package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.codec.StreamCodecNBT
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.nbt.BinaryTag

open class ServerboundCustomClickActionPacket(
    val id: String,
    val payload: BinaryTag?
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ServerboundCustomClickActionPacket::id,
            StreamCodecNBT.STREAM.optional(), ServerboundCustomClickActionPacket::payload,
            ::ServerboundCustomClickActionPacket
        )
    }
}