package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.sound.Sound
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.LocationCodecs
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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