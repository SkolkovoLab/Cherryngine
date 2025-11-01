package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundCustomQueryAnswerPacket(
    val messageId: Int,
    val data: ByteArray? = null,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundCustomQueryAnswerPacket::messageId,
            StreamCodec.RAW_BYTES_ARRAY.optional(), ServerboundCustomQueryAnswerPacket::data,
            ::ServerboundCustomQueryAnswerPacket
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerboundCustomQueryAnswerPacket

        if (messageId != other.messageId) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}