package ru.cherryngine.engine.core.commandmanager.brigadier

import com.google.common.collect.Queues
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntMaps
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import org.incendo.cloud.internal.CommandNode
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.flag.CommandFlagParser
import org.incendo.cloud.parser.standard.*
import org.incendo.cloud.parser.standard.StringParser.StringMode
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.commandmanager.SArgumentParser
import ru.cherryngine.engine.core.commandmanager.commands.args.KeyParser
import ru.cherryngine.engine.core.commandmanager.commands.args.LocationParser
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundCommandsPacket
import ru.cherryngine.lib.minecraft.protocol.types.ArgumentParserType
import java.util.*

object CommandNodeUtils {
    fun commandsPacket(rootNode: CommandNode<CommandSender>): ClientboundCommandsPacket {
        val wrapper = WrappedNode(rootNode, null, null, null)
        val object2IntMap = traverse(wrapper)
        val nodes = collectNodes(object2IntMap)
        val rootIndex = object2IntMap.getInt(rootNode)
        return ClientboundCommandsPacket(nodes, rootIndex)
    }

    private fun traverse(commandTree: WrappedNode): Object2IntMap<WrappedNode> {
        val object2IntMap = Object2IntOpenHashMap<WrappedNode>()
        val queue = Queues.newArrayDeque<WrappedNode>()
        queue.add(commandTree)

        queue.pollForEach { commandNode ->
            if (!object2IntMap.containsKey(commandNode)) {
                val i: Int = object2IntMap.size
                object2IntMap.put(commandNode, i)
                queue.addAll(commandNode.wrappedChildren)
            }
        }

        return object2IntMap
    }

    private fun collectNodes(nodes: Object2IntMap<WrappedNode>): List<ClientboundCommandsPacket.Node> {
        val objectArrayList = ObjectArrayList<ClientboundCommandsPacket.Node>(nodes.size)
        objectArrayList.size(nodes.size)

        Object2IntMaps.fastIterable(nodes).forEach { entry ->
            objectArrayList[entry.intValue] = createPacketNode(entry.key, nodes)
        }

        return objectArrayList
    }

    private fun createPacketNode(
        node: WrappedNode,
        nodes: Object2IntMap<WrappedNode>,
    ): ClientboundCommandsPacket.Node {
        val redirectedNode: Int?
        val children: List<Int>
        if (node.redirect != null) {
            redirectedNode = nodes.getInt(node.redirect)
            children = emptyList()
        } else {
            redirectedNode = null
            children = node.wrappedChildren.map(nodes::getInt)
        }

        return when (node.parser) {
            null -> ClientboundCommandsPacket.RootNode(children)

            is LiteralParser<CommandSender> -> ClientboundCommandsPacket.LiteralNode(
                children,
                redirectedNode,
                node.name!!,
                node.node.command() != null
            )

            else -> ClientboundCommandsPacket.ArgumentNode(
                children,
                redirectedNode,
                node.name!!,
                node.node.command() != null,
                argumentParser(node.parser),
                if ((node.parser as? SArgumentParser<*>)?.serverSuggestions != false) "minecraft:ask_server" else null
            )
        }
    }

    private fun argumentParser(parser: ArgumentParser<CommandSender, *>): ArgumentParserType {
        return when (parser) {
            is BooleanParser -> ArgumentParserType.Bool

            is FloatParser -> ArgumentParserType.Float(
                if (parser.hasMin()) parser.range().min() else null,
                if (parser.hasMax()) parser.range().max() else null,
            )

            is DoubleParser -> ArgumentParserType.Double(
                if (parser.hasMin()) parser.range().min() else null,
                if (parser.hasMax()) parser.range().max() else null,
            )

            is IntegerParser, is ByteParser, is ShortParser -> ArgumentParserType.Integer(
                if (parser.hasMin()) parser.range().min().toInt() else null,
                if (parser.hasMax()) parser.range().max().toInt() else null,
            )

            is LongParser -> ArgumentParserType.Long(
                if (parser.hasMin()) parser.range().min() else null,
                if (parser.hasMax()) parser.range().max() else null,
            )

            is StringParser -> when (parser.stringMode()) {
                StringMode.SINGLE -> ArgumentParserType.GameProfile
                StringMode.QUOTED -> ArgumentParserType.String(ArgumentParserType.String.Type.QUOTABLE_PHRASE)
                StringMode.GREEDY, StringMode.GREEDY_FLAG_YIELDING -> ArgumentParserType.String(ArgumentParserType.String.Type.GREEDY_PHRASE)
            }

            is CommandFlagParser, is StringArrayParser -> ArgumentParserType.String(ArgumentParserType.String.Type.GREEDY_PHRASE)

            is KeyParser -> ArgumentParserType.ResourceLocation

            is LocationParser -> ArgumentParserType.Vec3

            is UUIDParser -> ArgumentParserType.UUID

            is SArgumentParser -> parser.argumentParserType

            else -> ArgumentParserType.GameProfile
        }
    }

    fun <T> Queue<T>.pollForEach(action: (T) -> Unit) {
        while (true) {
            val element = this.poll() ?: break
            action(element)
        }
    }
}
