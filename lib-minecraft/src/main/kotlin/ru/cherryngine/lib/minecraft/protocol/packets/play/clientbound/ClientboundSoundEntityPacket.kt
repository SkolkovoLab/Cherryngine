package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.sound.Sound
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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