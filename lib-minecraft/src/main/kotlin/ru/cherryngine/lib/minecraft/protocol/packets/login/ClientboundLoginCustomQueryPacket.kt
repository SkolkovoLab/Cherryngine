package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundLoginCustomQueryPacket(
    val messageId: Int,
    val channel: String,
    val data: ByteArray,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundLoginCustomQueryPacket::messageId,
            StreamCodec.STRING, ClientboundLoginCustomQueryPacket::channel,
            StreamCodec.RAW_BYTES_ARRAY, ClientboundLoginCustomQueryPacket::data,
            ::ClientboundLoginCustomQueryPacket
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClientboundLoginCustomQueryPacket

        if (messageId != other.messageId) return false
        if (channel != other.channel) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = messageId
        result = 31 * result + channel.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}