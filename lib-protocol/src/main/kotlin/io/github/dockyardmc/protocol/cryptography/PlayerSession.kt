package io.github.dockyardmc.protocol.cryptography

import io.github.dockyardmc.tide.stream.StreamCodec
import kotlinx.datetime.Instant
import java.util.*

class PlayerSession(
    val sessionId: UUID,
    val expiry: Instant,
    val publicKey: ByteArray,
    val keySignature: ByteArray
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

