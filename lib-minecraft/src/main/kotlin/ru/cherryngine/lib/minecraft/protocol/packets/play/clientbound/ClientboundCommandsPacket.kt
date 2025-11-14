package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ArgumentParserType
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import kotlin.experimental.or

data class ClientboundCommandsPacket(
    val nodes: List<Node>,
    val rootIndex: Int,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Node.STREAM_CODEC.list(), ClientboundCommandsPacket::nodes,
            StreamCodec.VAR_INT, ClientboundCommandsPacket::rootIndex,
            ::ClientboundCommandsPacket
        )
    }

    sealed interface Node {
        val children: List<Int>
        val executable: Boolean get() = false
        val hasRedirect: Boolean get() = false
        val hasSuggestionsType: Boolean get() = false

        companion object {
            val STREAM_CODEC = object : StreamCodec<Node> {
                override fun write(buffer: ByteBuf, value: Node) {
                    var flags: Byte = when (value) {
                        is RootNode -> 0x00
                        is LiteralNode -> 0x01
                        is ArgumentNode -> 0x02
                    }
                    if (value.executable) flags = flags or 0x04
                    if (value.hasRedirect) flags = flags or 0x08
                    if (value.hasSuggestionsType) flags = flags or 0x10
                    StreamCodec.BYTE.write(buffer, flags)
                    StreamCodec.VAR_INT.list().write(buffer, value.children)

                    if (value is LiteralNode) value.redirectedNode?.let { StreamCodec.VAR_INT.write(buffer, it) }
                    if (value is ArgumentNode) value.redirectedNode?.let { StreamCodec.VAR_INT.write(buffer, it) }

                    if (value is LiteralNode) value.name.let { StreamCodec.STRING.write(buffer, it) }
                    if (value is ArgumentNode) value.name.let { StreamCodec.STRING.write(buffer, it) }

                    if (value is ArgumentNode) {
                        ArgumentParserType.STREAM_CODEC.write(buffer, value.parser)
                        if (value.suggestionsType != null) {
                            StreamCodec.STRING.write(buffer, value.suggestionsType)
                        }
                    }
                }

                override fun read(buffer: ByteBuf): Node {
                    TODO("Not yet implemented")
                }
            }
        }
    }


    data class RootNode(
        override val children: List<Int>,
    ) : Node

    data class LiteralNode(
        override val children: List<Int>,
        val redirectedNode: Int?,
        val name: String,
        override val executable: Boolean,
    ) : Node {
        override val hasRedirect: Boolean
            get() = redirectedNode != null
    }

    data class ArgumentNode(
        override val children: List<Int>,
        val redirectedNode: Int?,
        val name: String,
        override val executable: Boolean,
        val parser: ArgumentParserType,
        val suggestionsType: String?,
    ) : Node {
        override val hasRedirect: Boolean
            get() = redirectedNode != null

        override val hasSuggestionsType: Boolean
            get() = suggestionsType != null
    }
}
