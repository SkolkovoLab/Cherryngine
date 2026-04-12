package ru.cherryngine.engine.mcprotocollib

import net.kyori.adventure.key.Key
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandNode
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandParser
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandType
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.StringProperties
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandsPacket
import org.incendo.cloud.parser.standard.LiteralParser
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.OptionalInt

object McProtocolLibCommandNodeUtils {
    fun commandsPacket(
        rootNode: org.incendo.cloud.internal.CommandNode<CommandSender>,
    ): ClientboundCommandsPacket {
        val literalNames = rootNode.children().flatMap { child ->
            val parser = child.component().parser()
            if (parser is LiteralParser) {
                listOf(child.component().name()) + parser.alternativeAliases()
            } else {
                emptyList()
            }
        }

        val nodes = mutableListOf<CommandNode>()

        // Index 0: shared greedy args node
        nodes.add(CommandNode(
            CommandType.ARGUMENT,
            true,
            false,
            intArrayOf(),
            OptionalInt.empty(),
            "args",
            CommandParser.STRING,
            StringProperties.GREEDY_PHRASE,
            Key.key("minecraft", "ask_server")
        ))

        // Indices 1..N: literal nodes
        val literalIndices = mutableListOf<Int>()
        for (name in literalNames) {
            literalIndices.add(nodes.size)
            nodes.add(CommandNode(
                CommandType.LITERAL,
                true,
                false,
                intArrayOf(0),
                OptionalInt.empty(),
                name,
                null,
                null,
                null
            ))
        }

        // Root node
        val rootIndex = nodes.size
        nodes.add(CommandNode(
            CommandType.ROOT,
            false,
            false,
            literalIndices.toIntArray(),
            OptionalInt.empty(),
            null,
            null,
            null,
            null
        ))

        return ClientboundCommandsPacket(nodes.toTypedArray(), rootIndex)
    }
}
