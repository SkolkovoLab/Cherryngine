package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundGameEventPacket(
    val event: GameEvent,
    val value: Float,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ByteEnumStreamCodec<GameEvent>(), ClientboundGameEventPacket::event,
            StreamCodec.FLOAT, ClientboundGameEventPacket::value,
            ::ClientboundGameEventPacket
        )
    }

    enum class GameEvent {
        NO_RESPAWN_BLOCK_AVAILABLE,
        END_RAINING,
        START_RAINING,
        CHANGE_GAME_MODE,
        WIN_GAME,
        DEMO_EVENT,
        ARROW_HIT_PLAYER,
        RAIN_LEVEL_CHANGE,
        THUNDER_LEVEL_CHANGE,
        PLAY_PUFFERFISH_STING,
        PLAY_ELDER_GUARDIAN_JUMPSCARE,
        ENABLE_RESPAWN_SCREEN,
        LIMITED_CRAFTING,
        START_WAITING_FOR_CHUNKS
    }
}