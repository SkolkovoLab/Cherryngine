package ru.cherryngine.lib.packetevents.injector

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.player.User
import io.netty.channel.Channel
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.server.NettyServer
import ru.cherryngine.lib.packetevents.handlers.PacketEventsDecoder
import ru.cherryngine.lib.packetevents.handlers.PacketEventsEncoder
import com.github.retrooper.packetevents.injector.ChannelInjector as PacketEventsChannelInjector

class VelocityPipelineInjector(
    private val nettyServer: NettyServer,
) : PacketEventsChannelInjector {
    override fun isServerBound(): Boolean = true

    override fun inject() {
        nettyServer.injectors += ServerConnectionInitializer
    }

    override fun uninject() = Unit

    override fun updateUser(channel: Any, user: User) {
        channel as Channel
        val decoder = channel.pipeline().get(PacketEvents.DECODER_NAME) as PacketEventsDecoder
        decoder.user = user

        val encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME) as PacketEventsEncoder
        encoder.user = user
    }

    override fun isPlayerSet(channel: Any?): Boolean {
        channel as Channel? ?: return false

        val encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME) as PacketEventsEncoder
        if (encoder.player != null) return true

        val decoder = channel.pipeline().get(PacketEvents.DECODER_NAME) as PacketEventsDecoder
        return decoder.player != null
    }

    override fun setPlayer(channel: Any, player: Any) {
        channel as Channel
        player as Connection

        println(channel.pipeline().names())
        val decoder = channel.pipeline().get(PacketEvents.DECODER_NAME) as PacketEventsDecoder
        decoder.player = player
        decoder.user.profile.uuid = player.gameProfile.uuid
        decoder.user.profile.name = player.gameProfile.username

        val encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME) as PacketEventsEncoder
        encoder.player = player
    }

    override fun isProxy(): Boolean = false
}
