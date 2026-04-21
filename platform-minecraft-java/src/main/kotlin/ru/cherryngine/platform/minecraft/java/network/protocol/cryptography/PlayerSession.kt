package ru.cherryngine.platform.minecraft.java.network.protocol.cryptography

import java.util.*
import kotlin.time.Instant

class PlayerSession(
    val sessionId: UUID,
    val expiry: Instant,
    val publicKey: ByteArray,
    val keySignature: ByteArray,
)
