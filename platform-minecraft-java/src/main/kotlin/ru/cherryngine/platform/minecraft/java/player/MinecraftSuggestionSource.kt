package ru.cherryngine.platform.minecraft.java.player

import net.minestom.server.network.packet.client.play.ClientTabCompletePacket
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.player.SuggestionRequest
import ru.cherryngine.engine.core.player.SuggestionSource

@InstanceSingleton(platform = "minecraft")
class MinecraftSuggestionSource : SuggestionSource<MinecraftPlayer> {
    override fun canHandle(target: Player): Boolean = target is MinecraftPlayer

    override fun pollSuggestions(player: MinecraftPlayer): List<SuggestionRequest> =
        player.packets<ClientTabCompletePacket>()
            .map { SuggestionRequest(it.transactionId, it.text.removePrefix("/")) }
}
