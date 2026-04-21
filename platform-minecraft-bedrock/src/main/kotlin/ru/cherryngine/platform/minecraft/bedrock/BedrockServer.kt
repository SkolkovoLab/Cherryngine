package ru.cherryngine.platform.minecraft.bedrock

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import jakarta.inject.Singleton
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption
import org.cloudburstmc.protocol.bedrock.BedrockPong
import org.cloudburstmc.protocol.bedrock.BedrockServerSession
import org.cloudburstmc.protocol.bedrock.codec.v944.Bedrock_v944
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.player.InstanceRouter
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerRouter
import java.net.InetSocketAddress

@Singleton
class BedrockServer(
    private val playerManager: PlayerManager,
    private val instanceRouter: InstanceRouter,
    private val playerRouter: PlayerRouter,
    private val config: BedrockConfig,
) : ApplicationEventListener<StartupEvent> {
    private val log = LoggerFactory.getLogger(BedrockServer::class.java)

    override fun onApplicationEvent(event: StartupEvent) {
        start()
    }

    fun start() {
        val bootstrap = ServerBootstrap()
            .channelFactory(RakChannelFactory.server(NioDatagramChannel::class.java))
            .group(NioEventLoopGroup())
            .option(RakChannelOption.RAK_ADVERTISEMENT, buildPong())
            .option(RakChannelOption.RAK_PACKET_LIMIT, 300)
            .option(RakChannelOption.RAK_GLOBAL_PACKET_LIMIT, 1000)
            .childHandler(object : BedrockServerInitializer() {
                override fun initSession(session: BedrockServerSession) {
                    log.info("Bedrock client connected: {}", session.socketAddress)
                    session.packetHandler = BedrockSessionHandler(
                        session, playerManager, instanceRouter, playerRouter
                    ) { /* onReady callback */ }
                }
            })
        bootstrap.bind(InetSocketAddress(config.host, config.port)).syncUninterruptibly()
        log.info("Bedrock server listening on {}:{}", config.host, config.port)
    }

    private fun buildPong() = BedrockPong()
        .edition("MCPE")
        .motd("Cherryngine")
        .subMotd("Cherryngine")
        .playerCount(0)
        .maximumPlayerCount(20)
        .gameType("Creative")
        .protocolVersion(Bedrock_v944.CODEC.protocolVersion)
        .version(Bedrock_v944.CODEC.minecraftVersion)
        .ipv4Port(config.port)
        .ipv6Port(19133)
        .toByteBuf()
}
