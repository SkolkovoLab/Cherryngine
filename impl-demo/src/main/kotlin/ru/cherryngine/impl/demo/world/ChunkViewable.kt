package ru.cherryngine.impl.demo.world

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundBlockUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSectionBlocksUpdatePacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.globalToSectionRelative
import ru.cherryngine.lib.minecraft.world.block.Block

class ChunkViewable(
    override val chunkPos: ChunkPos,
    val chunk: Chunk,
) : StaticViewable {
    private val viewers = mutableSetOf<Connection>()
    override val viewerPredicate: (Connection) -> Boolean = { true }

    fun setBlock(blockPos: Vec3I, block: Block, dimensionType: DimensionType) {
        chunk.setBlock(blockPos, block, dimensionType)
        viewers.forEach {
            it.sendPacket(ClientboundBlockUpdatePacket(blockPos, block))
        }
    }

    fun setBlocks(blocks: Map<Vec3I, Block>, dimensionType: DimensionType) {
        blocks.forEach { (pos, block) ->
            chunk.setBlock(pos, block, dimensionType)
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

                it.sendPacket(ClientboundSectionBlocksUpdatePacket(sectionIndex, localSectionBlocks))
            }
        }
    }

    override fun show(player: Connection) {
        player.sendPacket(ClientboundLevelChunkWithLightPacket(chunkPos, chunk.chunkData, chunk.light))
        viewers.add(player)
    }

    override fun hide(player: Connection) {
        player.sendPacket(ClientboundForgetLevelChunkPacket(chunkPos))
        viewers.remove(player)
    }
}

