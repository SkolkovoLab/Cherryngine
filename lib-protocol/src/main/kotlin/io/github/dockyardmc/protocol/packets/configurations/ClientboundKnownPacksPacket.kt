package io.github.dockyardmc.protocol.packets.configurations

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.key.Key

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
