//package ru.cherryngine.lib.minecraft.protocol.packets.configurations
//
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.resourcepack.ResourcePack
//
//data class ClientboundConfigurationAddResourcePackPacket(val resourcePack: ResourcePack) : ClientboundPacket() {
//
//    init {
//        ResourcePack.STREAM_CODEC.write(buffer, resourcePack)
//    }
//}