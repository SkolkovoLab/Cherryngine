package ru.cherryngine.lib.minecraft.network.protocol.cryptography

import java.util.UUID
import kotlin.time.Instant

class PlayerSession(
    val sessionId: UUID,
    val expiry: Instant,
    val publicKey: ByteArray,
    val keySignature: ByteArray,
)
