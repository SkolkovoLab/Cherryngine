package ru.cherryngine.platform.minecraft.java.player

import net.minestom.server.network.packet.client.play.ClientCommandChatPacket
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.player.CommandSource
import ru.cherryngine.engine.core.player.Player

@InstanceSingleton(platform = "minecraft")
class MinecraftCommandSource : CommandSource<MinecraftPlayer> {
    override fun canHandle(target: Player): Boolean = target is MinecraftPlayer

    override fun pollCommands(player: MinecraftPlayer): List<String> =
        player.packets<ClientCommandChatPacket>().map { it.message }
}
