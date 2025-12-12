package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import net.kyori.adventure.nbt.BinaryTag
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.BinaryTagStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

open class ServerboundCustomClickActionPacket(
    val id: String,
    val payload: BinaryTag?
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ServerboundCustomClickActionPacket::id,
            BinaryTagStreamCodecs.STREAM.optional(), ServerboundCustomClickActionPacket::payload,
            ::ServerboundCustomClickActionPacket
        )
    }
}