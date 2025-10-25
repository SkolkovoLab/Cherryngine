package ru.cherryngine.lib.minecraft.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.PacketHandler
import ru.cherryngine.lib.minecraft.protocol.decoders.PacketLengthDecoder
import ru.cherryngine.lib.minecraft.protocol.decoders.RawPacketDecoder
import ru.cherryngine.lib.minecraft.protocol.encoders.PacketLengthEncoder
import ru.cherryngine.lib.minecraft.protocol.encoders.RawPacketEncoder
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ClientboundPacketRegistry
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ServerboundPacketRegistry
import java.net.InetSocketAddress

class NettyServer(
    val ip: String,
    val port: Int,

    val clientboundPacketRegistry: ClientboundPacketRegistry,
    val serverboundPacketRegistry: ServerboundPacketRegistry,
) {
    private val logger = LoggerFactory.getLogger(NettyServer::class.java)
    val bossGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())
    val workerGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())

    fun start(packetHandler: PacketHandler) {
        val bootstrap = ServerBootstrap()
        val channelInitializer = object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(channel: SocketChannel) {
                val connection = Connection(packetHandler)
                val pipeline = channel.pipeline()
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
            }
        }
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(channelInitializer)

        bootstrap.bind(InetSocketAddress(ip, port)).await()



        logger.info("Server running on ${ip}:${port}")
    }
}