package ru.cherryngine.engine.mcprotocollib

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import org.geysermc.mcprotocollib.auth.GameProfile
import org.geysermc.mcprotocollib.network.event.server.ServerAdapter
import org.geysermc.mcprotocollib.network.event.server.SessionAddedEvent
import org.geysermc.mcprotocollib.network.event.server.SessionRemovedEvent
import org.geysermc.mcprotocollib.network.server.NetworkServer
import org.geysermc.mcprotocollib.protocol.MinecraftConstants
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol
import org.geysermc.mcprotocollib.protocol.ServerLoginHandler
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.player.InstanceRouter
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerRouter
import ru.cherryngine.engine.core.services.PlayerService
import ru.cherryngine.engine.core.services.WorldService
import java.net.InetSocketAddress

@Singleton
class McProtocolLibServer(
    private val playerManager: PlayerManager,
    private val playerService: PlayerService,
    private val worldService: WorldService,
    private val instanceRouter: InstanceRouter,
    private val playerRouter: PlayerRouter,
    private val config: McProtocolLibConfig,
) : ApplicationEventListener<StartupEvent> {
    private val log = LoggerFactory.getLogger(McProtocolLibServer::class.java)

    override fun onApplicationEvent(event: StartupEvent) {
        start()
    }

    fun start() {
        val server = NetworkServer(
            InetSocketAddress(config.host, config.port),
            ::MinecraftProtocol
        )

        server.setGlobalFlag(MinecraftConstants.SHOULD_AUTHENTICATE, false)
        server.setGlobalFlag(MinecraftConstants.SERVER_COMPRESSION_THRESHOLD, 256)

        server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, ServerLoginHandler { session ->
            val profile = session.getFlag(MinecraftConstants.PROFILE_KEY) as GameProfile
            log.info("McProtocolLib player logged in: {} ({})", profile.name, profile.id)

            val player = McProtocolLibPlayer(session)
            playerManager.register(player)
            playerService.onPlayerJoin(player)
            worldService.onPlayerJoin(player)
            instanceRouter.routePlayer(player.uuid, playerRouter.getInitialInstance(player))
        })

        server.addListener(object : ServerAdapter() {
            override fun sessionAdded(event: SessionAddedEvent) {
                event.session.addListener(McProtocolLibRegistryInterceptor())
                event.session.addListener(McProtocolLibSessionListener(playerManager))
            }

            override fun sessionRemoved(event: SessionRemovedEvent) {
                val profile = event.session.getFlag(MinecraftConstants.PROFILE_KEY) as? GameProfile ?: return
                val player = playerManager.getPlayerNullable(profile.id) ?: return
                log.info("McProtocolLib player disconnected: {} ({})", profile.name, profile.id)
                instanceRouter.removePlayer(profile.id)
                worldService.onPlayerLeave(player)
                playerService.onPlayerLeave(player)
                playerManager.unregister(profile.id)
            }
        })

        server.bind()
        log.info("McProtocolLib server listening on {}:{}", config.host, config.port)
    }
}
