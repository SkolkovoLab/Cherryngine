package ru.cherryngine.lib.minecraft.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.protocol.decoders.PacketLengthDecoder
import ru.cherryngine.lib.minecraft.protocol.decoders.RawPacketDecoder
import ru.cherryngine.lib.minecraft.protocol.encoders.PacketLengthEncoder
import ru.cherryngine.lib.minecraft.protocol.encoders.RawPacketEncoder
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ClientboundPacketRegistry
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ServerboundPacketRegistry
import java.net.InetSocketAddress

class NettyServer(
    val clientboundPacketRegistry: ClientboundPacketRegistry,
    val serverboundPacketRegistry: ServerboundPacketRegistry,
) {
    private val logger = LoggerFactory.getLogger(NettyServer::class.java)
    val bossGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())
    val workerGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())

    var injectors = mutableSetOf<ChannelInjector>()

    private var started = false

    fun start(
        ip: String,
        port: Int,
        mojangAuth: Boolean,
        compressionThreshold: Int,
        connectionHandler: ConnectionHandler,
    ) {
        if (started) throw IllegalStateException()
        started = true
        val bootstrap = ServerBootstrap()
        val channelInitializer = object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(channel: SocketChannel) {
                val connection = Connection(connectionHandler, mojangAuth, compressionThreshold)
                channel.pipeline()
                    //encoders
                    .addFirst(
                        ChannelHandlers.RAW_PACKET_ENCODER,
                        RawPacketEncoder(connection, clientboundPacketRegistry)
                    )
                    .addFirst(
                        ChannelHandlers.RAW_PACKET_DECODER,
                        RawPacketDecoder(connection, serverboundPacketRegistry)
                    )
                    .addBefore(
                        ChannelHandlers.RAW_PACKET_DECODER,
                        ChannelHandlers.PACKET_LENGTH_DECODER,
                        PacketLengthDecoder()
                    )
                    .addBefore(
                        ChannelHandlers.RAW_PACKET_ENCODER,
                        ChannelHandlers.PACKET_LENGTH_ENCODER,
                        PacketLengthEncoder()
                    )
                    .addLast(ChannelHandlers.PLAYER_NETWORK_MANAGER, connection)

                injectors.forEach { it.inject(channel) }
            }
        }
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(channelInitializer)

        bootstrap.bind(InetSocketAddress(ip, port)).sync()

        logger.info("NettyServer running on ${ip}:${port}")
    }
}