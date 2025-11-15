package ru.cherryngine.integration.grim

import io.micronaut.context.annotation.Factory
import io.micronaut.context.event.ApplicationEventListener
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ServerboundFinishConfigurationPacket
import ru.cherryngine.lib.packetevents.PacketEventsImpl
import ru.cherryngine.lib.packetevents.initPacketEvents

@Factory
class PacketEventsFactory {
    @Singleton
    fun getPacketEvents(minecraftServer: MinecraftServer): PacketEventsImpl {
        return initPacketEvents(minecraftServer.nettyServer)
    }

    @Singleton
    class PacketEventsInitializer(
        val pe: PacketEventsImpl,
    ) : ApplicationEventListener<PacketEvent> {
        override fun onApplicationEvent(event: PacketEvent) {
            if (event.packet is ServerboundFinishConfigurationPacket) {
                pe.onPlayerLogin(event.connection)
            }
        }
    }
}