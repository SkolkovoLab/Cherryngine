package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.FixedBitSetStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.FixedRawBytesStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*
import kotlin.time.Instant

data class ServerboundChatPacket(
    val message: String,
    val timestamp: Instant,
    val salt: Long,
    val signature: ByteArray?,
    val ackOffset: Int,
    val ackList: BitSet,
    val checksum: Byte
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ServerboundChatPacket::message,
            StreamCodec.INSTANT, ServerboundChatPacket::timestamp,
            StreamCodec.LONG, ServerboundChatPacket::salt,
            FixedRawBytesStreamCodec(256).optional(), ServerboundChatPacket::signature,
            StreamCodec.VAR_INT, ServerboundChatPacket::ackOffset,
            FixedBitSetStreamCodec(20), ServerboundChatPacket::ackList,
            StreamCodec.BYTE, ServerboundChatPacket::checksum,
            ::ServerboundChatPacket
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerboundChatPacket

        if (salt != other.salt) return false
        if (ackOffset != other.ackOffset) return false
        if (checksum != other.checksum) return false
        if (message != other.message) return false
        if (timestamp != other.timestamp) return false
        if (!signature.contentEquals(other.signature)) return false
        if (ackList != other.ackList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = salt.hashCode()
        result = 31 * result + ackOffset
        result = 31 * result + checksum
        result = 31 * result + message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + (signature?.contentHashCode() ?: 0)
        result = 31 * result + ackList.hashCode()
        return result
    }
}