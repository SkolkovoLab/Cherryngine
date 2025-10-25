package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundEncryptionResponsePacket(
    val sharedSecret: ByteArray,
    val verifyToken: ByteArray
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BYTE_ARRAY, ServerboundEncryptionResponsePacket::sharedSecret,
            StreamCodec.BYTE_ARRAY, ServerboundEncryptionResponsePacket::verifyToken,
            ::ServerboundEncryptionResponsePacket
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerboundEncryptionResponsePacket

        if (!sharedSecret.contentEquals(other.sharedSecret)) return false
        if (!verifyToken.contentEquals(other.verifyToken)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sharedSecret.contentHashCode()
        result = 31 * result + verifyToken.contentHashCode()
        return result
    }
}