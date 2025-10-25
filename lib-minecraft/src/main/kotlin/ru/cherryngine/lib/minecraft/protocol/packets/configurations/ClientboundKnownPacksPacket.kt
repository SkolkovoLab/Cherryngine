package ru.cherryngine.lib.minecraft.protocol.packets.configurations

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundKnownPacksPacket(
    val knownPacks: List<KnownPack>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            KnownPack.STREAM_CODEC.list(), ClientboundKnownPacksPacket::knownPacks,
            ::ClientboundKnownPacksPacket
        )
    }

    data class KnownPack(
        val identifier: Key,
        val version: String
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.KEY_TWO_STRINGS, KnownPack::identifier,
                StreamCodec.STRING, KnownPack::version,
                ::KnownPack
            )
        }
    }
}
