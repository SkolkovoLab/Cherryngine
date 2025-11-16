package ru.cherryngine.integration.grim

import ac.grim.grimac.GrimAPI
import ac.grim.grimac.api.GrimAPIProvider
import ac.grim.grimac.api.plugin.BasicGrimPlugin
import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.PlatformLoader
import ac.grim.grimac.platform.api.PlatformServer
import ac.grim.grimac.platform.api.manager.*
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler
import ac.grim.grimac.platform.api.sender.SenderFactory
import com.github.retrooper.packetevents.PacketEventsAPI
import io.micronaut.context.event.ApplicationEventListener
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Singleton
import org.incendo.cloud.CommandManager
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.integration.grim.command.CommandManagerImpl
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundClientTickEndPacket
import java.io.File
import java.util.logging.Logger
import ac.grim.grimac.platform.api.sender.Sender as GrimSender

@Singleton
class PlatformLoaderImpl(
    private val packetEvents: PacketEventsAPI<*>,
    private val platformScheduler: PlatformScheduler,
    private val platformPlayerFactory: PlatformPlayerFactory,
    private val commandAdapter: CommandAdapter,
    private val commandManager: CommandManagerImpl,
    private val itemResetHandler: ItemResetHandler,
    private val senderFactory: SenderFactory<CommandSender>,
    private val platformPluginManager: PlatformPluginManager,
    private val platformServer: PlatformServer,
    private val messagePlaceHolderManager: MessagePlaceHolderManager,
    private val permissionRegistrationManager: PermissionRegistrationManager,
) : PlatformLoader, ApplicationEventListener<PacketEvent> {
    private val julLogger = Logger.getLogger(PlatformLoaderImpl::class.java.name)
    private val plugin: GrimPlugin = BasicGrimPlugin(
        julLogger,
        File("./grim/"),
        "dev",
        "",
        listOf()
    )

    @PostConstruct
    fun init() {
        System.setProperty("grim.platform-override", "bukkit")
        GrimAPI.INSTANCE.load(this)
        GrimAPI.INSTANCE.start()
        commandManager.init()
    }

    @PreDestroy
    fun destroy() {
        GrimAPI.INSTANCE.stop()
    }

    override fun getPlugin(): GrimPlugin = plugin
    override fun getPacketEvents(): PacketEventsAPI<*> = packetEvents
    override fun getScheduler(): PlatformScheduler = platformScheduler
    override fun getPlatformPlayerFactory(): PlatformPlayerFactory = platformPlayerFactory
    override fun getCommandAdapter(): CommandAdapter = commandAdapter
    override fun getCommandManager(): CommandManager<GrimSender> = commandManager
    override fun getItemResetHandler(): ItemResetHandler = itemResetHandler
    override fun getSenderFactory(): SenderFactory<*> = senderFactory
    override fun getPluginManager(): PlatformPluginManager = platformPluginManager
    override fun getPlatformServer(): PlatformServer = platformServer
    override fun getMessagePlaceHolderManager(): MessagePlaceHolderManager = messagePlaceHolderManager
    override fun getPermissionManager(): PermissionRegistrationManager = permissionRegistrationManager

    override fun registerAPIService() {
        GrimAPIProvider.init(GrimAPI.INSTANCE.externalAPI)
    }

    override fun onApplicationEvent(event: PacketEvent) {
        when (event.packet) {
            is ServerboundClientTickEndPacket -> {
                val grimPlayer = GrimAPI.INSTANCE.playerDataManager.getPlayer(event.connection.gameProfile.uuid) ?: return
                grimPlayer.checkManager.entityReplication.onEndOfTickEvent()
            }
        }
    }
}