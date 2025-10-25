package io.github.dockyardmc.protocol.types

import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.key.Key

enum class FeatureFlags {
    VANILLA,
    TRADE_REBALANCE,
    REDSTONE_EXPERIMENTS,
    MINECART_IMPROVEMENTS;

    companion object {
        val STREAM_CODEC = StreamCodec.Companion.KEY.transform<FeatureFlags>(
            { valueOf(it.value().uppercase()) },
            { Key.key(it.name.lowercase()) }
        )
    }
}