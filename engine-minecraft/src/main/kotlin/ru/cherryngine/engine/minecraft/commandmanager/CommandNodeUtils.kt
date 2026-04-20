package ru.cherryngine.engine.minecraft.commandmanager

import net.minestom.server.network.packet.server.play.DeclareCommandsPacket
import org.incendo.cloud.internal.CommandNode
import ru.cherryngine.engine.core.commandmanager.CommandSender

object CommandNodeUtils {
    /**
     * TODO: воссоздать генерацию DeclareCommandsPacket поверх Minestom-овской Node-модели.
     * Пока возвращает пустой пакет — tab-complete / клиентский sync команд не работает.
     */
    fun commandsPacket(rootNode: CommandNode<CommandSender>): DeclareCommandsPacket {
        val root = DeclareCommandsPacket.Node().apply {
            flags = 0
            children = IntArray(0)
        }
        return DeclareCommandsPacket(listOf(root), 0)
    }
}
