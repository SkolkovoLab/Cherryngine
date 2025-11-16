package ru.cherryngine.integration.grim

import com.github.retrooper.packetevents.PacketEvents
import io.micronaut.context.annotation.Factory
import io.micronaut.context.event.ApplicationEventListener
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.integration.grim.packetevents.PacketEventsImpl
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket

@Factory
class PacketEventsFactory {
    @Singleton
    fun getPacketEvents(minecraftServer: MinecraftServer): PacketEventsImpl {
        val api = PacketEventsImpl(minecraftServer.nettyServer)
        PacketEvents.setAPI(api)
        PacketEvents.getAPI().load()
        PacketEvents.getAPI().init()
        return api
    }

    @Singleton
    class PacketEventsInitializer(
        private val pe: PacketEventsImpl,
    ) : ApplicationEventListener<PacketEvent> {
        override fun onApplicationEvent(event: PacketEvent) {
            if (event.packet is ServerboundFinishConfigurationPacket) {
                pe.onPlayerLogin(event.connection)
            }
        }
    }
}