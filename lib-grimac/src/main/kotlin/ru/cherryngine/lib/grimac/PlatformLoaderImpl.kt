package ru.cherryngine.lib.grimac

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
import org.incendo.cloud.CommandManager
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import java.io.File

class PlatformLoaderImpl(
    val packetEvents: PacketEventsAPI<*>
) : PlatformLoader {
    private val logger = LoggerFactory.getLogger(PlatformLoaderImpl::class.java)
    private val plugin: GrimPlugin

    init {
        plugin = BasicGrimPlugin(
            Slf4jToJulAdapter(logger),
            File("./grim/"),
            "dev",
            "",
            listOf()
        )
    }

    fun load() {
        GrimAPI.INSTANCE.load(this)
    }

    fun start() {
        GrimAPI.INSTANCE.start()
    }

    fun stop() {
        GrimAPI.INSTANCE.stop()
    }

    override fun getScheduler(): PlatformScheduler {
        TODO("Not yet implemented")
    }

    override fun getPlatformPlayerFactory(): PlatformPlayerFactory {
        TODO("Not yet implemented")
    }

    override fun getCommandAdapter(): CommandAdapter {
        TODO("Not yet implemented")
    }

    override fun getPacketEvents(): PacketEventsAPI<*> {
        return packetEvents
    }

    override fun getCommandManager(): CommandManager<Sender> {
        TODO("Not yet implemented")
    }

    override fun getItemResetHandler(): ItemResetHandler {
        TODO("Not yet implemented")
    }

    override fun getSenderFactory(): SenderFactory<*> {
        TODO("Not yet implemented")
    }

    override fun getPlugin(): GrimPlugin {
        TODO("Not yet implemented")
    }

    override fun getPluginManager(): PlatformPluginManager {
        TODO("Not yet implemented")
    }

    override fun getPlatformServer(): PlatformServer {
        TODO("Not yet implemented")
    }

    override fun registerAPIService() {
        TODO("Not yet implemented")
    }

    override fun getMessagePlaceHolderManager(): MessagePlaceHolderManager {
        TODO("Not yet implemented")
    }

    override fun getPermissionManager(): PermissionRegistrationManager {
        TODO("Not yet implemented")
    }
}