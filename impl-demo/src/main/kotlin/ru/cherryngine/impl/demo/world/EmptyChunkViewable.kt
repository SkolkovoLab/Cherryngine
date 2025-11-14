package ru.cherryngine.impl.demo.world

import ru.cherryngine.impl.demo.Player
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData

data class EmptyChunkViewable(
    override val chunkPos: ChunkPos,
    val dimensionType: DimensionType,
) : StaticViewable {
    override val viewerPredicate: (Player) -> Boolean = { true }

    override fun show(player: Player) {
        player.connection.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, ChunkData.empty(dimensionType), Light.EMPTY))
    }

    override fun hide(player: Player) {
        player.connection.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
    }
}