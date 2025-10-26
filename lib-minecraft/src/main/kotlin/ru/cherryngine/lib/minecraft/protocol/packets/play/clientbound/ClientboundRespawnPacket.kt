package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.WorldPosition
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.registry.registries.DimensionTypeRegistry
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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
            DimensionTypeRegistry.STREAM_CODEC, ClientboundRespawnPacket::dimensionType,
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