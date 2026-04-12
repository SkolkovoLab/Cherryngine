package ru.cherryngine.engine.minecraft.commandmanager

import org.incendo.cloud.internal.CommandNode
import org.incendo.cloud.parser.standard.LiteralParser
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundCommandsPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ArgumentParserType

object CommandNodeUtils {
    fun commandsPacket(rootNode: CommandNode<CommandSender>): ClientboundCommandsPacket {
        val literalNames = rootNode.children().flatMap { child ->
            val parser = child.component().parser()
            if (parser is LiteralParser) {
                listOf(child.component().name()) + parser.alternativeAliases()
            } else {
                emptyList()
            }
        }

        val nodes = mutableListOf<ClientboundCommandsPacket.Node>()

        // Index 0: shared greedy args node
        nodes.add(
            ClientboundCommandsPacket.ArgumentNode(
            children = emptyList(),
            redirectedNode = null,
            name = "args",
            executable = true,
            parser = ArgumentParserType.String(ArgumentParserType.String.Type.GREEDY_PHRASE),
            suggestionsType = "minecraft:ask_server"
        ))

        // Indices 1..N: literal nodes
        val literalIndices = mutableListOf<Int>()
        for (name in literalNames) {
            literalIndices.add(nodes.size)
            nodes.add(
                ClientboundCommandsPacket.LiteralNode(
                children = listOf(0),
                redirectedNode = null,
                name = name,
                executable = true
            ))
        }

        // Last index: root node
        val rootIndex = nodes.size
        nodes.add(ClientboundCommandsPacket.RootNode(children = literalIndices))

        return ClientboundCommandsPacket(nodes, rootIndex)
    }
}