# Cherryngine

### [Russian README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README.md)

So yeah, in the future this is going to be a mega-innovative core for building servers.

You've seen CounterMine — and if you haven't, go check it out: https://cherry.pizza/

The first version of the core — the one used in CounterMine — is based on [Minestom](https://github.com/Minestom/Minestom), but we ran into a bunch of problems:
- No multithreading — if TPS drops in one world, all worlds start lagging
- Not flexible enough — ViewSystem only works with Entity, while we use composite models
- No Netty — kills the ability to plug in ViaVersion

On top of Minestom's issues, we have our own pain points. The project grew so fat that adding new features turned into a complete nightmare.

So I started building a new core (this one right here) — from scratch, without Minestom.

---

## What's inside

**`lib-minecraft`** — low-level implementation of Minecraft 1.21.11 protocol on top of Netty. Based on [Dockyard](https://github.com/DockyardMC/Dockyard), but heavily rewritten. If you need a Kotlin library for raw protocol interaction — here it is.

**`engine-core`** — toolset on top of `lib-minecraft`. DI via Micronaut. Connection management, chunk sending, Layer System.

**`engine-ecs`** — ECS built on [Fleks](https://github.com/Quillraven/Fleks). All game logic lives here.

**`engine-physics`** — physics via [Jolt Physics](https://github.com/jrouwe/JoltPhysics) through JNI. Terrain generation inspired by [Rayon](https://github.com/lazurite-dev/rayon) — blocks are added to the physics world dynamically around active bodies.

**`lib-world`** — Layer System. The most non-trivial part of the engine.

---

## Layer System

Native per-player world view — without copying the world.

Examples:
- **GTA-style apartments** — each player has their own apartment at the same coordinates. From outside it's invisible, inside it's only yours.
- **Build battle** — each player has their own canvas, during voting everyone looks at the same canvas.
- **Physics** — a dropped item collides with the blocks of *your* apartment, not someone else's.

Works through `viewContextIDs` (what a player sees) and `physContextIDs` (what physically collides).

---

## Multi-version

ViaVersion + ViaBackwards via `engine-integration:viaversion`. Server runs on 1.21.11 protocol, clients can connect from older versions.

---

## Demo

https://github.com/SkolkovoLab/CherryngineDemo

---

## Thanks
- [Minestom](https://github.com/Minestom/Minestom) — for finally ending Bukkit's monopoly and showing that you can build a large Minecraft server without NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — for the codebase that `lib-minecraft` is built on
- [Rayon](https://github.com/lazurite-dev/rayon) — for the block physics reference
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — for my childhood. A legend.
- Marsik — for not puking on the bed today 🥰