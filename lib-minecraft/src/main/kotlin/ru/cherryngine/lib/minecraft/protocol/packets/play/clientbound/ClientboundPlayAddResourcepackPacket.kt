//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.resourcepack.ResourcePack
//
//class ClientboundPlayAddResourcepackPacket(resourcepack: ResourcePack) : ClientboundPacket() {
//
//    init {
//        ResourcePack.STREAM_CODEC.write(buffer, resourcepack)
//    }
//}