package ru.cherryngine.lib.packetevents

import com.github.retrooper.packetevents.PacketEvents
import ru.cherryngine.lib.minecraft.server.NettyServer

fun initPacketEvents(nettyServer: NettyServer): PacketEventsImpl {
    val api = PacketEventsImpl(nettyServer)
    PacketEvents.setAPI(api)
    PacketEvents.getAPI().load()
    PacketEvents.getAPI().init()
    return api
}
