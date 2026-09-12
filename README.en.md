# Vindicator Slayer [卫道士克星]

> English | [中文](./README.md)

A NeoForge weapon mod for Minecraft 1.21.1. Adds "Vindicator Slayer", a sword that deals
bonus damage to the raider faction (pillager, vindicator, evoker, ravager, witch, illusioner).
Crafting: 2 diamonds + 3 netherite ingots.(This project was built with heavy AI assistance — almost everything was generated with AI tools, then tested and reviewed by me.)

## Features

- +4 bonus damage against raiders
- Falling strikes trigger a **smash attack** (damage scales with fall height, capped at +10; stacks with Density, effective fall capped at 10 blocks)
- Smash hits don't knock back the target; self-relaunch is clamped for stable combos
- Dedicated creative tab and two advancements

## Build

Requires Java 21. Open in IntelliJ IDEA (Gradle sync handles the rest), or:

```bash
gradle build
```

Built jar lands in `build/libs/`.

## Version history note

Sources for v1.0.0 – v1.3.0 were lost to a disk failure (binaries remain downloadable on Modrinth). Repository source starts at v1.3.1, recovered via decompilation and verified by a full rebuild that reproduces the shipped jar (37/37 entries identical).

## License

MIT
