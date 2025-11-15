package ru.cherryngine.engine.core.world

import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundBlockUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.globalToSectionRelative
import ru.cherryngine.lib.minecraft.world.block.Block

class ChunkViewable(
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
        blocks.entries.groupBy { (pos, _) ->
            ChunkUtils.sectionIndexFromBlockPos(pos)
        }.forEach { (sectionIndex, sectionBlocks) ->
            viewers.forEach {
                val localSectionBlocks = sectionBlocks.map { (pos, block) ->
                    ChunkUtils.encodeBlockData(
                        block.getProtocolId(),
                        globalToSectionRelative(pos.x),
                        globalToSectionRelative(pos.y),
                        globalToSectionRelative(pos.z)
                    )
                }

                it.connection.sendPacket(ClientboundSectionBlocksUpdatePacket(sectionIndex, localSectionBlocks))
            }
        }
    }

    override fun getBlock(pos: Vec3I): Block {
        return chunk.getBlock(pos)
    }

    override fun show(player: Player) {
        player.connection.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, chunk.chunkData, chunk.light))
        viewers.add(player)
    }

    override fun hide(player: Player) {
        player.connection.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
        viewers.remove(player)
    }
}

