package ru.cherryngine.engine.minecraft.commandmanager

import net.minestom.server.command.ArgumentParserType
import net.minestom.server.network.packet.server.play.DeclareCommandsPacket
import org.incendo.cloud.internal.CommandNode
import org.incendo.cloud.parser.standard.LiteralParser
import ru.cherryngine.engine.core.commandmanager.CommandSender

object CommandNodeUtils {
    /**
     * Собирает плоский `DeclareCommandsPacket`: на каждую cloud-литералу (плюс её алиасы)
     * делаем отдельный literal-node, у всех children — единственный argument-node с
     * `GREEDY_PHRASE` и `minecraft:ask_server`. Парс/сьюджестион делает сервер через
     * `ClientCommandChatPacket` / `ClientTabCompletePacket`, поэтому настоящее дерево
     * аргументов клиенту не нужно.
     */
    fun commandsPacket(rootNode: CommandNode<CommandSender>): DeclareCommandsPacket {
        val literalNames = rootNode.children().flatMap { child ->
            val parser = child.component().parser()
            if (parser is LiteralParser) {
                listOf(child.component().name()) + parser.alternativeAliases()
            } else {
                emptyList()
            }
        }

        val nodes = mutableListOf<DeclareCommandsPacket.Node>()

        // index 0: общий greedy args node
        nodes.add(DeclareCommandsPacket.Node().apply {
            flags = DeclareCommandsPacket.getFlag(
                DeclareCommandsPacket.NodeType.ARGUMENT,
                /* executable = */ true,
                /* redirect = */ false,
                /* suggestionType = */ true,
            )
            children = IntArray(0)
            name = "args"
            parser = ArgumentParserType.STRING
            // для brigadier:string properties — VarInt с режимом 0=SINGLE_WORD, 1=QUOTABLE, 2=GREEDY
            properties = byteArrayOf(0x02)
            suggestionsType = "minecraft:ask_server"
        })

        // indices 1..N: literal nodes с child = index 0
        val literalIndices = mutableListOf<Int>()
        for (literalName in literalNames) {
            literalIndices.add(nodes.size)
            nodes.add(DeclareCommandsPacket.Node().apply {
                flags = DeclareCommandsPacket.getFlag(
                    DeclareCommandsPacket.NodeType.LITERAL,
                    /* executable = */ true,
                    /* redirect = */ false,
                    /* suggestionType = */ false,
                )
                children = intArrayOf(0)
                name = literalName
            })
        }

        // root node в конце
        val rootIndex = nodes.size
        nodes.add(DeclareCommandsPacket.Node().apply {
            flags = DeclareCommandsPacket.getFlag(
                DeclareCommandsPacket.NodeType.ROOT,
                /* executable = */ false,
                /* redirect = */ false,
                /* suggestionType = */ false,
            )
            children = literalIndices.toIntArray()
        })

        return DeclareCommandsPacket(nodes, rootIndex)
    }
}
