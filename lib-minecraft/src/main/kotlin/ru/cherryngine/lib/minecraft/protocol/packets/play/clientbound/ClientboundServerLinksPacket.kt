//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.protocol.types.writeList
//import ru.cherryngine.lib.minecraft.apis.serverlinks.ServerLink
//
//class ClientboundServerLinksPacket(serverLinks: Collection<ServerLink>) : ClientboundPacket() {
//
//    init {
//        buffer.writeList(serverLinks, ServerLink::write)
//    }
//}
//
