# Cherryngine

### [Russian README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README.md)

A Kotlin framework for Minecraft servers with a platform-agnostic architecture. Supports Java Edition and Bedrock Edition; in theory, support for any other platform can be implemented — from Hytale to Team Fortress.

## Key ideas

**Cross-platform** — any client-facing functionality is expressed through an interface. "Render a model", "move a player", "show HP" — each platform has its own implementation, while gameplay code only touches the contract.

**Instance** — the unit of isolation. Lobby, match, arena, apartment — each instance is fully isolated: its own physics, its own ECS world, its own players. Multiple instances within a single JVM process share heavy objects (chunk caches, etc.).

**ECS** (as in Entity Component System, not [EliteClubSessions](https://www.youtube.com/@eliteclubsessions)) — the recommended way to write gameplay, but not mandatory. Want to — use Fleks. Don't want to — any class implementing `Tickable` becomes part of the game loop.

**Per-instance DI** — beans annotated with `@InstanceSingleton` are created once per instance, lazily, and destroyed together with it. Platform-specific beans are filtered by `platform` — only what's needed ends up in the instance:

```kotlin
@InstanceSingleton(platform = "minecraft")
class MinecraftAxolotlRenderer : AxolotlRenderer
// created only if the instance includes the "minecraft" platform
```

## Module structure

```
engine-core      — platform-agnostic core
engine-ecs       — optional ECS (Fleks)
engine-physics   — physics (Jolt), platform-independent
platform-minecraft-java    — Minecraft Java Edition
platform-minecraft-bedrock — Minecraft Bedrock Edition
```

## Demo project

https://github.com/SkolkovoLab/CherryngineDemo

---

## Credits

- [Minestom](https://github.com/Minestom/Minestom) — for ending the Bukkit monopoly and showing us that a large-scale Minecraft server can be built without NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — for the code base of the `lib-minecraft` module
- [Rayon](https://github.com/lazurite-dev/rayon) — for the block physics reference
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — for the childhood, a legend
- Marsik — for not puking on the bed today 🥰
