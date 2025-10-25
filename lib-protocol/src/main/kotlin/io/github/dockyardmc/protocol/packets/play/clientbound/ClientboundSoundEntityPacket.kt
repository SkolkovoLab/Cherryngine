package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.SoundEvent
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.sound.Sound

data class ClientboundSoundEntityPacket(
    val soundEvent: SoundEvent,
    val source: Sound.Source,
    val entityId: Int,
    val volume: Float,
    val pitch: Float,
    val seed: Long,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, ClientboundSoundEntityPacket::soundEvent,
            EnumStreamCodec<Sound.Source>(), ClientboundSoundEntityPacket::source,
            StreamCodec.VAR_INT, ClientboundSoundEntityPacket::entityId,
            StreamCodec.FLOAT, ClientboundSoundEntityPacket::volume,
            StreamCodec.FLOAT, ClientboundSoundEntityPacket::pitch,
            StreamCodec.LONG, ClientboundSoundEntityPacket::seed,
            ::ClientboundSoundEntityPacket
        )
    }
}