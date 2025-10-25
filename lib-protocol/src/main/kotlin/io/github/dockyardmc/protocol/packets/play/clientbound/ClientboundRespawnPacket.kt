package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.GameMode
import io.github.dockyardmc.protocol.types.WorldPosition
import io.github.dockyardmc.registry.registries.DimensionType
import io.github.dockyardmc.registry.registries.DimensionTypeRegistry
import io.github.dockyardmc.tide.stream.ByteEnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.key.Key

data class ClientboundRespawnPacket(
    val dimensionType: DimensionType,
    val dimensionName: Key,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val isDebug: Boolean,
    val isFlat: Boolean,
    val deathLocation: WorldPosition?,
    val portalCooldown: Int,
    val seaLevel: Int,
    val dataKept: RespawnDataKept,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(DimensionTypeRegistry), ClientboundRespawnPacket::dimensionType,
            StreamCodec.KEY, ClientboundRespawnPacket::dimensionName,
            StreamCodec.LONG, ClientboundRespawnPacket::hashedSeed,
            ByteEnumStreamCodec<GameMode>(), ClientboundRespawnPacket::gameMode,
            ByteEnumStreamCodec<GameMode>(), ClientboundRespawnPacket::previousGameMode,
            StreamCodec.BOOLEAN, ClientboundRespawnPacket::isDebug,
            StreamCodec.BOOLEAN, ClientboundRespawnPacket::isFlat,
            WorldPosition.STREAM_CODEC.optional(), ClientboundRespawnPacket::deathLocation,
            StreamCodec.VAR_INT, ClientboundRespawnPacket::portalCooldown,
            StreamCodec.VAR_INT, ClientboundRespawnPacket::seaLevel,
            RespawnDataKept.STREAM_CODEC, ClientboundRespawnPacket::dataKept,
            ::ClientboundRespawnPacket
        )
    }

    data class RespawnDataKept(
        val keepAttributes: Boolean,
        val keepMetadata: Boolean,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.byteFlags(
                0x01, RespawnDataKept::keepAttributes,
                0x02, RespawnDataKept::keepMetadata,
                ::RespawnDataKept
            )
        }
    }
}