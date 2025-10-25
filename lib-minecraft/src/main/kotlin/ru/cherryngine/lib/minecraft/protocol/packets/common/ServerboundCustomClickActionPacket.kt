package ru.cherryngine.lib.minecraft.protocol.packets.common

import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.codec.StreamCodecNBT
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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