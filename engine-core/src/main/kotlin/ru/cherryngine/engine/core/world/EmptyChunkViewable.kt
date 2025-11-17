package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.Light
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData

data class EmptyChunkViewable(
    override val chunkPos: ChunkPos,
    val dimensionType: DimensionType,
) : BlocksViewable {
    override val viewerPredicate: (Player) -> Boolean = { true }

    override fun show(player: Player) {
        player.connection.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, ChunkData.empty(dimensionType), Light.EMPTY))
    }

    override fun hide(player: Player) {
        player.connection.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
    }

    override fun getBlockId(pos: Vec3I): Int {
        return 0
    }

    override fun getBlock(pos: Vec3I): Block {
        return Block.AIR
    }
}