package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameMode
import ru.cherryngine.lib.minecraft.protocol.types.WorldPosition
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.registry.registries.DimensionTypeRegistry
import ru.cherryngine.lib.minecraft.tide.stream.ByteEnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundLoginPacket(
    val entityId: Int,
    val isHardcore: Boolean,
    val dimensionNames: List<Key>,
    val maxPlayers: Int,
    val viewDistance: Int,
    val simulationDistance: Int,
    val reducedDebugInfo: Boolean,
    val enableRespawnScreen: Boolean,
    val doLimitedCrafting: Boolean,
    val dimensionType: DimensionType,
    val dimensionName: String,
    val hashedSeed: Long,
    val gameMode: GameMode,
    val previousGameMode: GameMode,
    val isDebug: Boolean,
    val isFlat: Boolean,
    val deathLocation: WorldPosition?,
    val portalCooldown: Int,
    val seaLevel: Int,
    val enforcesSecureChat: Boolean,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT, ClientboundLoginPacket::entityId,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::isHardcore,
            StreamCodec.KEY.list(), ClientboundLoginPacket::dimensionNames,
            StreamCodec.VAR_INT, ClientboundLoginPacket::maxPlayers,
            StreamCodec.VAR_INT, ClientboundLoginPacket::viewDistance,
            StreamCodec.VAR_INT, ClientboundLoginPacket::simulationDistance,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::reducedDebugInfo,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::enableRespawnScreen,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::doLimitedCrafting,
            DimensionTypeRegistry.STREAM_CODEC, ClientboundLoginPacket::dimensionType,
            StreamCodec.STRING, ClientboundLoginPacket::dimensionName,
            StreamCodec.LONG, ClientboundLoginPacket::hashedSeed,
            ByteEnumStreamCodec<GameMode>(), ClientboundLoginPacket::gameMode,
            ByteEnumStreamCodec<GameMode>(), ClientboundLoginPacket::previousGameMode,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::isDebug,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::isFlat,
            WorldPosition.STREAM_CODEC.optional(), ClientboundLoginPacket::deathLocation,
            StreamCodec.VAR_INT, ClientboundLoginPacket::portalCooldown,
            StreamCodec.VAR_INT, ClientboundLoginPacket::seaLevel,
            StreamCodec.BOOLEAN, ClientboundLoginPacket::enforcesSecureChat,
            ::ClientboundLoginPacket
        )
    }

}