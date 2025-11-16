package ru.cherryngine.integration.grim.packetevents.injector

import ac.grim.grimac.utils.viaversion.ViaVersionUtil
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.UserConnectEvent
import com.github.retrooper.packetevents.protocol.ConnectionState
import com.github.retrooper.packetevents.protocol.PacketSide
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import ru.cherryngine.integration.grim.packetevents.handler.PacketEventsDecoder
import ru.cherryngine.integration.grim.packetevents.handler.PacketEventsEncoder
import ru.cherryngine.lib.minecraft.server.ChannelHandlers
import ru.cherryngine.lib.minecraft.server.ChannelInjector
import ru.cherryngine.lib.viaversion.ViaChannelHandlers

object ServerConnectionInitializer : ChannelInjector {
    override fun inject(channel: Channel) {
        val user = User(channel, ConnectionState.HANDSHAKING, null, UserProfile(null, null))
        val connectEvent = UserConnectEvent(user)

        PacketEvents.getAPI().eventManager.callEvent(connectEvent)

        if (connectEvent.isCancelled) {
            channel.unsafe().closeForcibly()
            return
        }

        val pipelineSide = PacketSide.SERVER
        channel.pipeline()
            .addBefore(
                ChannelHandlers.RAW_PACKET_DECODER,
                PacketEvents.DECODER_NAME,
                PacketEventsDecoder(pipelineSide, user, false)
            )
            .addBefore(
                ChannelHandlers.RAW_PACKET_ENCODER,
                PacketEvents.ENCODER_NAME,
                PacketEventsEncoder(pipelineSide, user, false)
            )

        if (PacketEvents.getAPI().settings.isPreViaInjection && ViaVersionUtil.isAvailable) {
            channel.pipeline()
                .addBefore(
                    ViaChannelHandlers.VIA_DECODER,
                    "pre-" + PacketEvents.DECODER_NAME,
                    PacketEventsDecoder(pipelineSide, user, true)
                )
                .addBefore(
                    ViaChannelHandlers.VIA_ENCODER,
                    "pre-" + PacketEvents.ENCODER_NAME,
                    PacketEventsEncoder(pipelineSide, user, true)
                )
        }

        channel.closeFuture().addListener(ChannelFutureListener { _ ->
            PacketEventsImplHelper.handleDisconnection(user.channel, user.uuid)
        })

        PacketEvents.getAPI().protocolManager.setUser(channel, user)
    }
}