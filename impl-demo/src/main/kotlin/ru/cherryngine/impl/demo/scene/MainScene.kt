package ru.cherryngine.impl.demo.scene

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket

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