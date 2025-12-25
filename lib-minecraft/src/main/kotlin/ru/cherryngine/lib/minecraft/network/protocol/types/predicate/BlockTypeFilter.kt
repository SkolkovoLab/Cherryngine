package ru.cherryngine.lib.minecraft.network.protocol.types.predicate

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.utils.Either
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

sealed interface BlockTypeFilter : Predicate<Block> {
    companion object {
            val CODEC: Codec<BlockTypeFilter> =
                Codec.either(Codec.STRING, Codec.STRING.list()).transform(
                    { either ->
                        when (either) {
                            is Either.Left -> {
                                val s = either.value
                                if (s.startsWith("#")) {
                                    Tag(s.removePrefix("#"))
                                } else {
                                    Blocks(listOf(Registries.block[s].value.toBlock()))
                                }
                            }
                            is Either.Right -> {
                                val ids = either.value
                                Blocks(ids.map { id -> Registries.block[id].value.toBlock() })
                            }
                        }
                    },
                    { filter ->
                        when (filter) {
                            is Blocks -> {
                                val ids = filter.blocks.map { it.identifier }
                                if (ids.size == 1) Either.Left(ids.first()) else Either.Right(ids)
                            }
                            is Tag -> {
                                Either.Left("#${filter.tag}")
                            }
                        }
                    }
                )


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
                            StreamCodec.VAR_INT.write(buffer, block.getStateId())
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
        val blocks: List<Block>,
    ) : BlockTypeFilter {
        override fun test(block: Block): Boolean {
            val blockId = block.getStateId()
            blocks.forEach { b ->
                if (blockId == b.getStateId()) return true
            }
            return false
        }
    }

    data class Tag(val tag: String) : BlockTypeFilter {
        override fun test(t: Block): Boolean {
            return false // TODO
        }
    }
}