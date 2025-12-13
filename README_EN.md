# Cherryngine

### [Russian README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README.md)

So yeah, in the future this is going to be a mega-innovative core for building servers.

You’ve seen CounterMine — and if you haven’t, go check it out: https://cherry.pizza/

The first version of the core — the one used in CounterMine — is based on [Minestom](https://github.com/Minestom/Minestom), but we ran into a bunch of Minestom issues:
- No multithreading. Specifically: if TPS drops in one of the worlds for whatever reason, *all* worlds start lagging. That’s absolutely not okay for us. Sure, we wrote a patch, but we really don’t want to live on crutches.
- Minestom isn’t flexible enough. For example, the ViewSystem only works with Entity, while we use composite models that we’d like to process as a single Viewable.
- No Netty — I honestly have no fucking clue why the Minestom devs decided to avoid Netty, but this kills the ability to plug in ViaVersion, and in general Netty is just a joy to work with.

On top of Minestom’s problems, we also have our own pain points. The project has grown so fat that adding new features has turned into a complete nightmare.

So I started developing a new core (this one right here).<br>
Since Minestom isn’t an option for us, we looked for alternatives and found [Dockyard](https://github.com/DockyardMC/Dockyard).<br>
It’s more flexible and uses Netty, but fuck — it has no fewer problems. Still, the foundation is solid.<br>
Long story short: I took Dockyard and literally cut out everything that doesn’t relate to minimal interaction with the game protocol, and almost everything that remained was rewritten.<br>
All of this lives in the `lib-minecraft` module. So if you need a Kotlin library for raw protocol interaction — here it is.

Next comes the `engine-core` module — basically a set of tools built on top of `lib-minecraft`, and it also includes a DI framework in the form of Micronaut.

So, the base is there. What’s left is to write something you can actually build game mechanics on. Architecturally, the choice fell on ECS (which, just in case, stands for Entity Component System, not [EliteClubSessions](https://www.youtube.com/@eliteclubsessions)).<br>
All of this lives in `engine-ecs`.

And here’s a small demo project: https://github.com/SkolkovoLab/CherryngineDemo

### Thanks
- [Minestom](https://github.com/Minestom/Minestom) — for finally putting an end to Bukkit’s monopoly and offering a fucking awesome alternative, and for opening our eyes to the fact that you *can* build a large Minecraft server without NMS.
- [Dockyard](https://github.com/DockyardMC/Dockyard) — for the codebase that the `lib-minecraft` module is built on.
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — for my childhood. A legend.
- Marsik — for not puking on the bed today 🥰
