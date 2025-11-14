package ru.cherryngine.lib.grimac.packetevents.injector

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.UserConnectEvent
import com.github.retrooper.packetevents.protocol.ConnectionState
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import ru.cherryngine.lib.grimac.packetevents.handlers.PacketEventsDecoder
import ru.cherryngine.lib.grimac.packetevents.handlers.PacketEventsEncoder
import ru.cherryngine.lib.minecraft.server.ChannelHandlers
import ru.cherryngine.lib.minecraft.server.ChannelInjector

object ServerConnectionInitializer : ChannelInjector {
    override fun inject(channel: Channel) {
        val user = User(channel, ConnectionState.HANDSHAKING, null, UserProfile(null, null))
        val connectEvent = UserConnectEvent(user)

        PacketEvents.getAPI().eventManager.callEvent(connectEvent)

        if (connectEvent.isCancelled) {
            channel.unsafe().closeForcibly()
            return
        }

        channel.pipeline().addBefore(ChannelHandlers.RAW_PACKET_DECODER, PacketEvents.DECODER_NAME, PacketEventsDecoder(user))
        channel.pipeline().addBefore(ChannelHandlers.RAW_PACKET_ENCODER, PacketEvents.ENCODER_NAME, PacketEventsEncoder(user))

        channel.closeFuture().addListener(ChannelFutureListener { _ ->
            PacketEventsImplHelper.handleDisconnection(user.channel, user.uuid)
        })

        PacketEvents.getAPI().protocolManager.setUser(channel, user)
    }
}