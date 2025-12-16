package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundBlockUpdatePacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Blocks
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.sectionIndexFromSectionPos
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.world.Chunk

class LayerChunkViewable(
    override val chunkPos: ChunkPos,
    val chunk: Chunk,
) : BlocksViewable {
    private val viewers = mutableSetOf<Player>()
    override val viewerPredicate: (Player) -> Boolean = { true }

    fun setBlock(blockPos: Vec3I, block: Block) {
        chunk.setBlock(blockPos, block)
        viewers.forEach {
            it.connection.sendPacket(ClientboundBlockUpdatePacket(blockPos, block))
        }
    }

    fun setBlocks(blocks: Map<Vec3I, Block>) {
        blocks.forEach { (pos, block) ->
            chunk.setBlock(pos, block)
        }
    }

    override fun show(player: Player) {
        val dimensionType = Registries.dimensionType["overworld"] // TODO оно должно браться откуда-нибудь
        val minSection = dimensionType.minY / 16
        val sVoidBlockId = Registries.block["structure_void"].defaultStateId
        chunk.sections.forEachIndexed { sectionIndex, section ->
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

    override fun getBlockId(pos: Vec3I): Int? {
        val block = chunk.getBlockId(pos)
        if (block == 0) return null
        if (block == Registries.block[Blocks.STRUCTURE_VOID].defaultStateId) return 0
        return block
    }

    override fun getBlock(pos: Vec3I): Block? {
        return getBlockId(pos)?.let { Block.getBlockByStateId(it) }
    }
}

