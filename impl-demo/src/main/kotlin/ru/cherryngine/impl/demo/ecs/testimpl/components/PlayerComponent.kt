package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.impl.demo.view.StaticViewableProvider
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.impl.demo.world.world.World
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.server.Connection

data class PlayerComponent(
    val connection: Connection,
    val packets: List<ServerboundPacket>,
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf(),
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf(),
    val prevWorld: World? = null,
    val world: World? = null,
) : GameComponent