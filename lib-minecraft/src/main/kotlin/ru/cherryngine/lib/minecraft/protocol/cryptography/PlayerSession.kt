package ru.cherryngine.lib.minecraft.protocol.cryptography

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*
import kotlin.time.Instant

class PlayerSession(
    val sessionId: UUID,
    val expiry: Instant,
    val publicKey: ByteArray,
    val keySignature: ByteArray,
) {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID, PlayerSession::sessionId,
            StreamCodec.INSTANT, PlayerSession::expiry,
            StreamCodec.BYTE_ARRAY, PlayerSession::publicKey,
            StreamCodec.BYTE_ARRAY, PlayerSession::keySignature,
            ::PlayerSession
        )
    }
}

