package io.github.dockyardmc.protocol.plugin

import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.protocol.plugin.messages.RegisterPluginMessage
import io.github.dockyardmc.protocol.plugin.messages.UnregisterPluginMessage

internal class PlayPluginMessageHandler {

    fun handleRegister(message: RegisterPluginMessage, networkManager: Connection) {
//        val event = RegisterPluginChannelsEvent(networkManager.player, message.channels, getPlayerEventContext(networkManager.player))
//        Events.dispatch(event)
//
//        networkManager.player.sendPluginMessage(RegisterPluginMessage(PluginMessageRegistry.getChannels(PluginMessageRegistry.Type.PLAY)))
//
//        // Noxesium integration
//        if (ConfigManager.config.implementationConfig.noxesium) {
//            if (message.channels.contains("${Noxesium.PACKET_NAMESPACE}:server_info")) {
//                Noxesium.addPlayer(event.player)
//            }
//        }
    }

    fun handleUnregister(message: UnregisterPluginMessage, networkManager: Connection) {
//        val event = UnregisterPluginChannelsEvent(networkManager.player, message.channels, getPlayerEventContext(networkManager.player))
//        Events.dispatch(event)
    }

}