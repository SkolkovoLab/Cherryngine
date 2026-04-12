package ru.cherryngine.engine.mcprotocollib

import jakarta.inject.Singleton
import net.kyori.adventure.key.Key
import org.cloudburstmc.math.vector.Vector3d
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerSpawnInfo
import org.geysermc.mcprotocollib.protocol.data.game.level.notify.GameEvent
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundGameEventPacket
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.services.PlayerServiceHandler

@Singleton
class McProtocolLibPlayerServiceHandler : PlayerServiceHandler {
    override fun canHandle(player: Player) = player is McProtocolLibPlayer

    override fun onPlayerJoin(player: Player) {
        val mcplPlayer = player as McProtocolLibPlayer
        val session = mcplPlayer.session

        session.send(
            ClientboundLoginPacket(
                0,
                false,
                arrayOf(Key.key("world")),
                20,
                8,
                8,
                false,
                true,
                false,
                PlayerSpawnInfo(
                    0,
                    Key.key("world"),
                    0L,
                    GameMode.CREATIVE,
                    GameMode.CREATIVE,
                    false,
                    false,
                    null,
                    0,
                    32
                ),
                false
            )
        )

        session.send(
            ClientboundPlayerPositionPacket(
                0,
                Vector3d.ZERO,
                Vector3d.ZERO,
                0f, 0f,
                emptyList()
            )
        )

        session.send(
            ClientboundGameEventPacket(GameEvent.LEVEL_CHUNKS_LOAD_START, null)
        )
    }

    override fun onPlayerLeave(player: Player) {}
}
