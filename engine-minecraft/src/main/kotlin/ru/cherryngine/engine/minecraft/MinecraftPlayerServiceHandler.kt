package ru.cherryngine.engine.minecraft

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.services.PlayerServiceHandler
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundGameEventPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundLoginPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.DimensionTypes

@Singleton
class MinecraftPlayerServiceHandler : PlayerServiceHandler {
    override fun canHandle(player: Player) = player is MinecraftPlayer

    override fun onPlayerJoin(player: Player) {
        val mcPlayer = player as MinecraftPlayer
        val connection = mcPlayer.connection

        connection.sendPacket(
            ClientboundLoginPacket(
                0,
                false,
                listOf(),
                20,
                8,
                8,
                false,
                true,
                false,
                Registries.dimensionType[DimensionTypes.OVERWORLD].value,
                "world",
                0L,
                GameMode.CREATIVE,
                GameMode.CREATIVE,
                false,
                false,
                null,
                0,
                32,
                false
            )
        )

        connection.sendPacket(
            ClientboundGameEventPacket(
                ClientboundGameEventPacket.GameEvent.START_WAITING_FOR_CHUNKS,
                0f
            )
        )
    }

    override fun onPlayerLeave(player: Player) {}
}
