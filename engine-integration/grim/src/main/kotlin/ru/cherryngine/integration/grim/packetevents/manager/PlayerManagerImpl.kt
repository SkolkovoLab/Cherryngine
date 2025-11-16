package ru.cherryngine.integration.grim.packetevents.manager

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract
import io.netty.channel.Channel
import ru.cherryngine.lib.minecraft.server.Connection

class PlayerManagerImpl : PlayerManagerAbstract() {
    override fun getPing(player: Any): Int {
        return 0 // TODO
    }

    override fun getChannel(player: Any): Channel {
        player as Connection
        val protocolManager: ProtocolManager = PacketEvents.getAPI().protocolManager
        var channel: Channel? = protocolManager.getChannel(player.gameProfile.uuid) as Channel?

        if (channel == null) {
            channel = player.channel

            synchronized(channel) {
                protocolManager.setChannel(player.gameProfile.uuid, channel)
            }
        }

        return channel
    }
}
