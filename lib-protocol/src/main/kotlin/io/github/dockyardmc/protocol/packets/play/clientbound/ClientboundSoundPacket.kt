package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.SoundEvent
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.sound.Sound

data class ClientboundSoundPacket(
    val soundEvent: SoundEvent,
    val source: Sound.Source,
    val location: Vec3D,
    val volume: Float,
    val pitch: Float,
    val seed: Long
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, ClientboundSoundPacket::soundEvent,
            EnumStreamCodec<Sound.Source>(), ClientboundSoundPacket::source,
            LocationCodecs.SOUND_LOCATION, ClientboundSoundPacket::location,
            StreamCodec.FLOAT, ClientboundSoundPacket::volume,
            StreamCodec.FLOAT, ClientboundSoundPacket::pitch,
            StreamCodec.LONG, ClientboundSoundPacket::seed,
            ::ClientboundSoundPacket
        )
    }
}