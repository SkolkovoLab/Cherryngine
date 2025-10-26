package ru.cherryngine.lib.minecraft.protocol.types

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

enum class FeatureFlags {
    VANILLA,
    TRADE_REBALANCE,
    REDSTONE_EXPERIMENTS,
    MINECART_IMPROVEMENTS;

    companion object {
        val STREAM_CODEC = StreamCodec.KEY.transform<FeatureFlags>(
            { valueOf(it.value().uppercase()) },
            { Key.key(it.name.lowercase()) }
        )
    }
}