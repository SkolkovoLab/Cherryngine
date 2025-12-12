package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import net.kyori.adventure.sound.Sound
import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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