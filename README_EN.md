# Cherryngine

### [Russian README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README.md)

A Minecraft server core built from scratch in Kotlin.

You've seen CounterMine? If not — check it out: https://cherry.pizza/
<br>
The first core was built on [Minestom](https://github.com/Minestom/Minestom), and we ran into several issues:

- No multithreading — lag in one world tanks TPS everywhere
- Inflexible ViewSystem — only works with Entity, but we need composite models
- No Netty — can't plug in ViaVersion

On top of that, the project grew so large that adding new features became a nightmare.

That's how Cherryngine was born — a new core from scratch, without Minestom.

---

## Key Ideas

**Cross-platform.** The architecture is protocol-independent: players from different platforms can play in the same instance simultaneously and interact with each other. Adding a new platform means writing one `@Singleton` handler — everything else stays untouched.

**Platform first, ECS is an adapter.** ECS systems don't know about Minecraft. Platform logic lives separately and doesn't leak into gameplay.

**Layer System.** Each player sees their own view of the world — without copying the world. GTA-style apartments, personal build-battle canvases, physics on per-player blocks — all out of the box.

---

## What's Inside

**`lib-minecraft`** — low-level Minecraft protocol implementation on top of Netty. Based on [Dockyard](https://github.com/DockyardMC/Dockyard), but heavily reworked. If you need a Kotlin lib for raw protocol interaction — here it is.

**`engine-core`** — protocol-independent core. `Instance`, `Tickable`, `Player` (interface), services with cross-platform dispatch.

**`engine-minecraft`** — Minecraft implementation on top of `engine-core`. Connection management, chunk delivery, McEntity, commands. Does not depend on ECS.

**`engine-ecs`** — ECS (as in Entity Component System, not [EliteClubSessions](https://www.youtube.com/@eliteclubsessions)) powered by [Fleks](https://github.com/Quillraven/Fleks). All gameplay logic lives here. Does not depend on Minecraft.

**`engine-physics`** — physics powered by [Jolt Physics](https://github.com/jrouwe/JoltPhysics) via JNI. Terrain generation inspired by [Rayon](https://github.com/lazurite-dev/rayon) — blocks are dynamically added to the physics world around active bodies.

**`lib-world`** — Layer System. The most non-trivial part of the core.

---

## Layer System

Native support for per-player world views — without copying the world.

- **GTA-style apartments** — each player has their own apartment at the same coordinates, invisible from outside
- **Build battle** — each player has their own canvas, during voting everyone sees the same one
- **Physics** — a thrown item collides with blocks in your apartment, not someone else's

Works through `viewContextIDs` (what the player sees) and `physContextIDs` (what they physically collide with).

---

## Multi-version

ViaVersion + ViaBackwards via `engine-integration:viaversion`. Clients can join from older versions.

---

## Demo

https://github.com/SkolkovoLab/CherryngineDemo

---

## Credits

- [Minestom](https://github.com/Minestom/Minestom) — for ending Bukkit's monopoly and opening our eyes to the fact that a large-scale Minecraft server can be built without NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — for the codebase behind `lib-minecraft`
- [Rayon](https://github.com/lazurite-dev/rayon) — for the block physics reference
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — for our childhood, a legend
- Marsik — for not throwing up on the bed today 🥰
