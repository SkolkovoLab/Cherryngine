package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundEncryptionRequestPacket(
    val serverID: String,
    val pubKey: ByteArray,
    val verToken: ByteArray,
    val shouldAuthenticate: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC= StreamCodec.of(
            StreamCodec.STRING, ClientboundEncryptionRequestPacket::serverID,
            StreamCodec.BYTE_ARRAY, ClientboundEncryptionRequestPacket::pubKey,
            StreamCodec.BYTE_ARRAY, ClientboundEncryptionRequestPacket::verToken,
            StreamCodec.BOOLEAN, ClientboundEncryptionRequestPacket::shouldAuthenticate,
            ::ClientboundEncryptionRequestPacket
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClientboundEncryptionRequestPacket

        if (serverID != other.serverID) return false
        if (!pubKey.contentEquals(other.pubKey)) return false
        if (!verToken.contentEquals(other.verToken)) return false
        if (shouldAuthenticate != other.shouldAuthenticate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = serverID.hashCode()
        result = 31 * result + pubKey.contentHashCode()
        result = 31 * result + verToken.contentHashCode()
        result = 31 * result + shouldAuthenticate.hashCode()
        return result
    }
}