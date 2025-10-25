package ru.cherryngine.impl.demo.scene

import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket

abstract class Scene {
    private val players = mutableSetOf<Player>()

    fun addPlayer(player: Player) {
        players.add(player)
        onAdd(player)
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        onRemove(player)
    }

    abstract fun onAdd(player: Player)
    abstract fun onRemove(player: Player)
    abstract fun onPacket(player: Player, packet: ServerboundPacket)
}