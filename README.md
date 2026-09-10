# [卫道士克星] Vindicator Slayer

A NeoForge weapon mod for Minecraft 1.21.1. Adds "Vindicator Slayer", a sword that deals
bonus damage to the raider faction (pillager, vindicator, evoker, ravager, witch, illusioner).

为 Minecraft 增加一把名叫"卫道士克星"的武器：攻击灾厄生物时伤害更高，可用剑与重锤两类附魔
（受原版附魔排斥性约束）。合成：2 钻石 + 3 下界合金锭。

## Features / 特性

- 对灾厄阵营额外 +4 伤害
- 高处下落命中触发**猛击**（伤害随下落高度增加，封顶 +10；致密附魔可叠加，有效下落封顶 10 格）
- 猛击命中不击退目标，自身回弹有上限（可稳定连招）
- 拥有专属创造模式标签页与两枚成就

## Build / 构建

Requires Java 21. Open in IntelliJ IDEA (Gradle sync handles the rest), or:

```bash
gradle build
```

Built jar lands in `build/libs/`.

## 版本历史说明 / Version history note

> v1.0.0 – v1.3.0 的源码因磁盘故障未能保留（成品 jar 均可在 Modrinth 下载）；本仓库源码自 v1.3.1 起，经反编译恢复并已通过完整重编译验证（产物与发布 jar 逐条目一致）。
>
> Sources for v1.0.0 – v1.3.0 were lost to a disk failure (binaries remain downloadable on Modrinth). Repository source starts at v1.3.1, recovered via decompilation and verified by a full rebuild that reproduces the shipped jar (37/37 entries identical).

## License

MIT
