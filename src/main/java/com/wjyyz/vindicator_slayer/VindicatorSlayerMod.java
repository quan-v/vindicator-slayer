package com.wjyyz.vindicator_slayer;

import com.wjyyz.vindicator_slayer.item.VindicatorSlayerSword;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Items;

@Mod("vindicator_slayer")
public class VindicatorSlayerMod {
   public static final String MOD_ID = "vindicator_slayer";
   public static final Items ITEMS = DeferredRegister.createItems("vindicator_slayer");
   public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "vindicator_slayer");
   public static final DeferredItem<Item> VINDICATOR_SLAYER = ITEMS.register("vindicator_slayer", () -> new VindicatorSlayerSword(new Properties()));
   public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VINDICATOR_SLAYER_TAB = CREATIVE_TABS.register(
      "vindicator_slayer_tab",
      () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.vindicator_slayer"))
            .icon(() -> new ItemStack((ItemLike)VINDICATOR_SLAYER.get()))
            .displayItems((params, output) -> output.accept((ItemLike)VINDICATOR_SLAYER.get()))
            .build()
   );

   public VindicatorSlayerMod(IEventBus bus) {
      ITEMS.register(bus);
      CREATIVE_TABS.register(bus);
      NeoForge.EVENT_BUS.register(VindicatorSlayerSword.class);
   }
}
