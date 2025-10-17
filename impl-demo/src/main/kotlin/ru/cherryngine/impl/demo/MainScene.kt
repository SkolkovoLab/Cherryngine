package ru.cherryngine.impl.demo

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import jakarta.inject.Singleton

@Singleton
class MainScene : Scene() {
    override fun onAdd(player: Player) {
        TODO("Not yet implemented")
    }

    override fun onRemove(player: Player) {
        TODO("Not yet implemented")
    }

    override fun onPacket(
        player: Player,
        packet: ServerboundPacket,
    ) {

    }
}