package ru.cherryngine.integration.grim

import com.github.retrooper.packetevents.PacketEventsAPI
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.packetevents.initPacketEvents

@Factory
class PacketEventsFactory {
    @Singleton
    fun getDockyardServer(minecraftServer: MinecraftServer): PacketEventsAPI<*> {
        return initPacketEvents(minecraftServer.nettyServer)
    }
}