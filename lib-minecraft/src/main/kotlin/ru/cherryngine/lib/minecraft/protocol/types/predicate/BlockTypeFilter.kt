package ru.cherryngine.lib.minecraft.protocol.types.predicate

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

interface BlockTypeFilter : Predicate<Block>, DataComponentHashable {
    override fun hashStruct(): HashHolder {
        val hash: Int = when (this) {
            is Blocks -> {
                if (blocks.size == 1) {
                    CRC32CHasher.ofString(blocks.first().identifier)
                } else {
                    CRC32CHasher.ofList(blocks.map { block -> CRC32CHasher.ofString(block.identifier) })
                }
            }

            is Tag -> {
                CRC32CHasher.ofString("#$tag")
            }

            else -> throw IllegalArgumentException("what. there is only 2 options")
        }
        return StaticHash(hash)
    }

    companion object {
        val STREAM_CODEC = object : StreamCodec<BlockTypeFilter> {
            override fun write(buffer: ByteBuf, value: BlockTypeFilter) {
                when (value) {
                    is Tag -> {
                        StreamCodec.VAR_INT.write(buffer, 0)
                        StreamCodec.STRING.write(buffer, value.tag)
                    }

                    is Blocks -> {
                        StreamCodec.VAR_INT.write(buffer, value.blocks.size + 1)
                        value.blocks.forEach { block ->
                            StreamCodec.VAR_INT.write(buffer, block.getProtocolId())
                        }
                    }
                }
            }

            override fun read(buffer: ByteBuf): BlockTypeFilter {
                val count = StreamCodec.VAR_INT.read(buffer) - 1

                return if (count == -1) {
                    Tag(StreamCodec.STRING.read(buffer))
                } else {
                    Blocks(List(count) {
                        Block.getBlockByStateId(StreamCodec.VAR_INT.read(buffer))
                    })
                }
            }
        }
    }

    data class Blocks(
        val blocks: List<Block>
    ) : BlockTypeFilter {
        override fun test(block: Block): Boolean {
            val blockId = block.getProtocolId()
            blocks.forEach { b ->
                if (blockId == b.getProtocolId()) return true
            }
            return false
        }
    }

    data class Tag(val tag: String) : BlockTypeFilter {
        override fun test(t: Block): Boolean {
            return t.registryBlock.tags.contains(tag)
        }
    }
}