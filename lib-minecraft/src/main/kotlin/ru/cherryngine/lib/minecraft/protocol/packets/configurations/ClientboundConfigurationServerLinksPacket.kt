//package ru.cherryngine.lib.minecraft.protocol.packets.configurations
//
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.protocol.types.writeList
//import ru.cherryngine.lib.minecraft.apis.serverlinks.ServerLink
//
//class ClientboundConfigurationServerLinksPacket(
//    serverLinks: Collection<ServerLink>
//) : ClientboundPacket() {
//
//    init {
//        buffer.writeList(serverLinks, ServerLink::write)
//    }
//}