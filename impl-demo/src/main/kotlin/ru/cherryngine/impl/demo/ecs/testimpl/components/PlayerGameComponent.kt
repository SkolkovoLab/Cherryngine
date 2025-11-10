package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.impl.demo.view.PlayerViewSystem
import ru.cherryngine.impl.demo.world.world.World
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket

data class PlayerGameComponent(
    val player: Player,
    val packets: List<ServerboundPacket>,
    val viewSystem: PlayerViewSystem,
    val prevWorld: World? = null,
    val world: World? = null,
) : GameComponent