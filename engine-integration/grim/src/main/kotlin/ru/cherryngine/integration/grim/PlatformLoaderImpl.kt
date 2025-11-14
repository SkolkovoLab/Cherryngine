package ru.cherryngine.integration.grim

import ac.grim.grimac.GrimAPI
import ac.grim.grimac.api.plugin.BasicGrimPlugin
import ac.grim.grimac.api.plugin.GrimPlugin
import ac.grim.grimac.platform.api.PlatformLoader
import ac.grim.grimac.platform.api.PlatformServer
import ac.grim.grimac.platform.api.manager.*
import ac.grim.grimac.platform.api.player.PlatformPlayerFactory
import ac.grim.grimac.platform.api.scheduler.PlatformScheduler
import ac.grim.grimac.platform.api.sender.Sender
import ac.grim.grimac.platform.api.sender.SenderFactory
import com.github.retrooper.packetevents.PacketEventsAPI
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Singleton
import org.incendo.cloud.CommandManager
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.commandmanager.CloudCommandManager
import ru.cherryngine.integration.grim.scheduler.PlatformSchedulerImpl
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import java.io.File

@Singleton
class PlatformLoaderImpl(
    private val packetEvents: PacketEventsAPI<*>,
    private val cloudCommandManager: CloudCommandManager,
) : PlatformLoader {
    private val logger = LoggerFactory.getLogger(PlatformLoaderImpl::class.java)
    private val plugin: GrimPlugin = BasicGrimPlugin(
        Slf4jToJulAdapter(logger),
        File("./grim/"),
        "dev",
        "",
        listOf()
    )

    @PostConstruct
    fun init() {
        GrimAPI.INSTANCE.load(this)
        GrimAPI.INSTANCE.start()
    }

    @PreDestroy
    fun destroy() {
        GrimAPI.INSTANCE.stop()
    }

    private val scheduler by lazy(::PlatformSchedulerImpl)
    override fun getScheduler(): PlatformScheduler = scheduler

    private val platformPlayerFactory = PlatformPlayerFactoryImpl()
    override fun getPlatformPlayerFactory(): PlatformPlayerFactory = platformPlayerFactory

    private val commandAdapter = CommandAdapterImpl()
    override fun getCommandAdapter(): CommandAdapter = commandAdapter

    override fun getPacketEvents(): PacketEventsAPI<*> = packetEvents

    private val commandManager by lazy { CommandManagerWrapper(cloudCommandManager, senderFactory) }
    override fun getCommandManager(): CommandManager<Sender> = commandManager

    private val itemResetHandler by lazy { ItemResetHandlerImpl() }
    override fun getItemResetHandler(): ItemResetHandler = itemResetHandler

    private val senderFactory by lazy { SenderFactoryImpl() }
    override fun getSenderFactory(): SenderFactory<*> = senderFactory

    override fun getPlugin(): GrimPlugin = plugin

    private val platformPluginManager = PlatformPluginManagerImpl()
    override fun getPluginManager(): PlatformPluginManager = platformPluginManager

    private val platformServer = PlatformServerImpl()
    override fun getPlatformServer(): PlatformServer = platformServer

    override fun registerAPIService() {
//        TODO("Not yet implemented")
    }

    private val messagePlaceHolderManager = MessagePlaceHolderManagerImpl()
    override fun getMessagePlaceHolderManager(): MessagePlaceHolderManager = messagePlaceHolderManager

    private val permissionRegistrationManager = PermissionRegistrationManagerImpl()
    override fun getPermissionManager(): PermissionRegistrationManager = permissionRegistrationManager
}