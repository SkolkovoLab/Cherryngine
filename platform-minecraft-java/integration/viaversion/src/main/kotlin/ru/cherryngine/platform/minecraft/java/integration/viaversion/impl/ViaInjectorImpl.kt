package ru.cherryngine.platform.minecraft.java.integration.viaversion.impl

import com.viaversion.viaversion.api.platform.ViaInjector
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion
import com.viaversion.viaversion.connection.UserConnectionImpl
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl
import io.netty.channel.Channel
import ru.cherryngine.platform.minecraft.java.ServerConsts
import ru.cherryngine.platform.minecraft.java.network.ChannelHandlers
import ru.cherryngine.platform.minecraft.java.network.ChannelInjector
import ru.cherryngine.platform.minecraft.java.network.NettyServer

class ViaInjectorImpl(
    private val nettyServer: NettyServer,
) : ViaInjector, ChannelInjector {
    override fun inject(channel: Channel) {
        val connection = UserConnectionImpl(channel, false)
        ProtocolPipelineImpl(connection)

        val encoder = ViaEncodeHandler(connection)
        val decoder = ViaDecodeHandler(connection)

        channel.pipeline()
            .addBefore(ChannelHandlers.RAW_PACKET_ENCODER, ViaChannelHandlers.VIA_ENCODER, encoder)
            .addBefore(ChannelHandlers.RAW_PACKET_DECODER, ViaChannelHandlers.VIA_DECODER, decoder)

        connection.isActive = true
    }

    override fun inject() {
        nettyServer.injectors += this
    }

    override fun uninject() {
        nettyServer.injectors -= this
    }

    override fun getServerProtocolVersion(): ProtocolVersion {
        return ProtocolVersion.getProtocol(ServerConsts.PROTOCOL_VERSION)
    }

    override fun getDump() = throw NotImplementedError()
}