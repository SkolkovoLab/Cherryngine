package ru.cherryngine.lib.packetevents

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.PacketEventsAPI
import com.github.retrooper.packetevents.event.UserLoginEvent
import com.github.retrooper.packetevents.injector.ChannelInjector
import com.github.retrooper.packetevents.manager.InternalPacketListener
import com.github.retrooper.packetevents.manager.player.PlayerManager
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager
import com.github.retrooper.packetevents.manager.server.ServerManager
import com.github.retrooper.packetevents.netty.NettyManager
import com.github.retrooper.packetevents.protocol.ProtocolVersion
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState
import com.github.retrooper.packetevents.settings.PacketEventsSettings
import com.github.retrooper.packetevents.util.LogManager
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.server.NettyServer
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import ru.cherryngine.lib.packetevents.injector.VelocityPipelineInjector
import ru.cherryngine.lib.packetevents.manager.PlayerManagerImpl
import ru.cherryngine.lib.packetevents.manager.ServerManagerImpl
import java.util.logging.Level

class PacketEventsImpl(
    nettyServer: NettyServer,
) : PacketEventsAPI<Unit>() {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val settings: PacketEventsSettings = PacketEventsSettings()

    private val protocolManager: ProtocolManager = object : ProtocolManagerAbstract() {
        override fun getPlatformVersion(): ProtocolVersion {
            return ProtocolVersion.UNKNOWN
        }
    }

    private val serverManager: ServerManager = ServerManagerImpl()
    private val playerManager: PlayerManagerAbstract = PlayerManagerImpl()
    private val injector: ChannelInjector = VelocityPipelineInjector(nettyServer)
    private val nettyManager: NettyManager = NettyManagerImpl()
    private val logManager: LogManager = object : LogManager() {
        override fun log(level: Level, color: NamedTextColor?, message: String) {
            Slf4jToJulAdapter.log(logger, level, message)
        }
    }

    private var loaded = false
    private var initialized = false
    private var terminated = false

    override fun load() {
        if (!loaded) {
            PacketEvents.IDENTIFIER = "pe"
            PacketEvents.ENCODER_NAME = "pe-encoder"
            PacketEvents.DECODER_NAME = "pe-decoder"
            PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler"
            PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer"
            PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler"
            WrappedBlockState.ensureLoad()
            injector.inject()
            loaded = true
            getEventManager().registerListener(InternalPacketListener())
        }
    }

    override fun isLoaded(): Boolean = loaded

    fun onPlayerLogin(player: Connection) {
        val channel = PacketEvents.getAPI().playerManager.getChannel(player) ?: return
        PacketEvents.getAPI().injector.setPlayer(channel, player)
        val user = PacketEvents.getAPI().playerManager.getUser(player) ?: return
        val loginEvent = UserLoginEvent(user, player)
        PacketEvents.getAPI().eventManager.callEvent(loginEvent)
    }

    override fun init() {
        load()
        if (initialized) return

        if (settings.shouldCheckForUpdates()) {
            getUpdateChecker().handleUpdateCheck()
        }

        PacketType.Play.Client.load()
        PacketType.Play.Server.load()
        initialized = true
    }

    override fun isInitialized(): Boolean = initialized

    override fun terminate() {
        if (initialized) {
            injector.uninject()
            getEventManager().unregisterAllListeners()
            initialized = false
            terminated = true
        }
    }

    override fun isTerminated(): Boolean = terminated

    override fun getLogManager(): LogManager = logManager
    override fun getPlugin(): Unit = Unit
    override fun getProtocolManager(): ProtocolManager = protocolManager
    override fun getServerManager(): ServerManager = serverManager
    override fun getPlayerManager(): PlayerManager = playerManager
    override fun getInjector(): ChannelInjector = injector
    override fun getSettings(): PacketEventsSettings = settings
    override fun getNettyManager(): NettyManager = nettyManager
}