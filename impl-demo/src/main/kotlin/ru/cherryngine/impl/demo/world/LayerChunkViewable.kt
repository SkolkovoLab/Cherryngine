package ru.cherryngine.impl.demo.world

import ru.cherryngine.impl.demo.Player
import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundBlockUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.Blocks
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.sectionIndexFromSectionPos
import ru.cherryngine.lib.minecraft.world.block.Block

class LayerChunkViewable(
    override val chunkPos: ChunkPos,
    val chunk: Chunk,
) : StaticViewable {
    private val viewers = mutableSetOf<Player>()
    override val viewerPredicate: (Player) -> Boolean = { true }

    fun setBlock(blockPos: Vec3I, block: Block, dimensionType: DimensionType) {
        chunk.setBlock(blockPos, block, dimensionType)
        viewers.forEach {
            it.connection.sendPacket(ClientboundBlockUpdatePacket(blockPos, block))
        }
    }

    fun setBlocks(blocks: Map<Vec3I, Block>, dimensionType: DimensionType) {
        blocks.forEach { (pos, block) ->
            chunk.setBlock(pos, block, dimensionType)
        }
    }

    override fun show(player: Player) {
        val dimensionType = DimensionTypes.OVERWORLD // TODO оно должно браться откуда-нибудь
        val minSection = dimensionType.minY / 16
        val sVoidBlockId = Blocks.STRUCTURE_VOID.getProtocolId()
        chunk.chunkData.sections.forEachIndexed { sectionIndex, section ->
            val blocks = mutableListOf<Long>()
            for (x in 0..<16) for (y in 0..<16) for (z in 0..<16) {
                var blockId = section.getBlock(x, y, z)
                if (blockId == 0) continue
                if (blockId == sVoidBlockId) blockId = 0
                blocks += ChunkUtils.encodeBlockData(blockId, x, y, z)
            }
            val sectionPos = Vec3I(chunkPos.x, sectionIndex + minSection, chunkPos.z)
            val sectionFullIndex = sectionIndexFromSectionPos(sectionPos)
            player.connection.sendPacket(ClientboundSectionBlocksUpdatePacket(sectionFullIndex, blocks))
        }

        viewers.add(player)
    }

    override fun hide(player: Player) {
        player.chunksToRefresh += chunkPos
        viewers.remove(player)
    }
}

