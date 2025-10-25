package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.Sound
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundStopSoundPacket(
    val category: Sound.Source?,
    val sound: String?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = object : StreamCodec<ClientboundStopSoundPacket> {
            override fun write(buffer: ByteBuf, value: ClientboundStopSoundPacket) {
                val hasCategory = value.category != null
                val hasSound = value.sound != null

                var flags = 0
                if (hasCategory) flags = flags or 0x1
                if (hasSound) flags = flags or 0x2
                buffer.writeByte(flags)

                if (hasCategory) EnumStreamCodec<Sound.Source>().write(buffer, value.category)
                if (hasSound) StreamCodec.STRING.write(buffer, value.sound)
            }

            override fun read(buffer: ByteBuf): ClientboundStopSoundPacket {
                val flags = buffer.readByte().toInt()
                val category = if ((flags and 0x1) != 0) EnumStreamCodec<Sound.Source>().read(buffer) else null
                val sound = if ((flags and 0x2) != 0) StreamCodec.STRING.read(buffer) else null
                return ClientboundStopSoundPacket(category, sound)
            }
        }
    }
}
