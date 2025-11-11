package ru.cherryngine.lib.via

import com.viaversion.viaversion.api.platform.ViaInjector
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion
import com.viaversion.viaversion.connection.UserConnectionImpl
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl
import io.netty.channel.Channel
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.minecraft.ServerConsts
import ru.cherryngine.lib.minecraft.server.ChannelHandlers
import ru.cherryngine.lib.minecraft.server.ChannelInjector

class ViaInjectorImpl(
    private val minecraftServer: MinecraftServer,
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
        minecraftServer.nettyServer.injectors += this
    }

    override fun uninject() {
        minecraftServer.nettyServer.injectors -= this
    }

    override fun getServerProtocolVersion(): ProtocolVersion {
        return ProtocolVersion.getProtocol(ServerConsts.PROTOCOL_VERSION)
    }

    override fun getDump() = throw NotImplementedError()
}