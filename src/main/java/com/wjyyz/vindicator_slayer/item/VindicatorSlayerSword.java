package com.wjyyz.vindicator_slayer.item;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Post;

public class VindicatorSlayerSword extends SwordItem {
   private static final float SMASH_FALL_THRESHOLD = 1.5F;
   private static final float SMASH_BONUS_CAP = 10.0F;
   private static final float DENSITY_FALL_CAP = 10.0F;
   private static final float RAIDERS_BONUS = 4.0F;
   private static final TagKey<EntityType<?>> RAIDERS_TAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("minecraft", "raiders"));
   private static Entity smashVictim = null;
   private static final float MAX_BOUNCE_Y = 1.55F;
   private static final Set<ServerPlayer> CLAMP_BOUNCE = Collections.newSetFromMap(new WeakHashMap<>());
   private static final List<Component> LORE = List.of(
      Component.translatable("item.vindicator_slayer.vindicator_slayer.lore1"),
      Component.translatable("item.vindicator_slayer.vindicator_slayer.lore2"),
      Component.translatable("item.vindicator_slayer.vindicator_slayer.lore3"),
      Component.translatable("item.vindicator_slayer.vindicator_slayer.lore4")
   );
   private static final Tier SLAYER_TIER = new Tier() {
      public int getUses() {
         return 1561;
      }

      public float getSpeed() {
         return 9.0F;
      }

      public float getAttackDamageBonus() {
         return 6.0F;
      }

      public int getEnchantmentValue() {
         return 15;
      }

      public Ingredient getRepairIngredient() {
         return Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT});
      }

      public TagKey<Block> getIncorrectBlocksForDrops() {
         return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
      }
   };

   public VindicatorSlayerSword(Properties properties) {
      super(SLAYER_TIER, properties.component(DataComponents.ATTRIBUTE_MODIFIERS, buildModifiers()).component(DataComponents.LORE, new ItemLore(LORE)));
   }

   private static ItemAttributeModifiers buildModifiers() {
      return createAttributes(SLAYER_TIER, 2, -2.4F)
         .withModifierAdded(
            Attributes.ENTITY_INTERACTION_RANGE,
            new AttributeModifier(ResourceLocation.fromNamespaceAndPath("vindicator_slayer", "reach"), 1.0, Operation.ADD_VALUE),
            EquipmentSlotGroup.MAINHAND
         );
   }

   public float getAttackDamageBonus(Entity target, float baseDamage, DamageSource source) {
      smashVictim = null;
      if (source.getDirectEntity() instanceof LivingEntity attacker) {
         float var10 = 0.0F;
         if (target.getType().is(RAIDERS_TAG)) {
            var10 += 4.0F;
         }

         if (isSmashAttack(attacker)) {
            smashVictim = target;
            float effFall = Math.min(attacker.fallDistance, 10.0F);
            float slam = effFall <= 3.0F ? 4.0F * effFall : (effFall <= 8.0F ? 12.0F + 2.0F * (effFall - 3.0F) : 22.0F + effFall - 8.0F);
            var10 += Math.min(slam, 10.0F);
            if (attacker.level() instanceof ServerLevel serverlevel) {
               var10 += EnchantmentHelper.modifyFallBasedDamage(serverlevel, attacker.getWeaponItem(), target, source, 0.0F) * effFall;
            }
         }

         return var10;
      } else {
         return 0.0F;
      }
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      if (attacker instanceof ServerPlayer serverplayer && isSmashAttack(serverplayer)) {
         serverplayer.setDeltaMovement(serverplayer.getDeltaMovement().with(Axis.Y, 0.01F));
         serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
         CLAMP_BOUNCE.add(serverplayer);
      }

      return super.hurtEnemy(stack, target, attacker);
   }

   public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
      super.postHurtEnemy(stack, target, attacker);
      if (isSmashAttack(attacker)) {
         attacker.resetFallDistance();
      }
   }

   private static boolean isSmashAttack(LivingEntity attacker) {
      return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
   }

   @SubscribeEvent
   public static void onKnockback(LivingKnockBackEvent event) {
      if (event.getEntity() == smashVictim) {
         event.setCanceled(true);
         smashVictim = null;
      }
   }

   @SubscribeEvent
   public static void onPlayerTick(Post event) {
      if (event.getEntity() instanceof ServerPlayer sp && CLAMP_BOUNCE.remove(sp) && sp.getDeltaMovement().y > 1.55F) {
         sp.setDeltaMovement(sp.getDeltaMovement().with(Axis.Y, 1.55F));
         sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
      }
   }
}
