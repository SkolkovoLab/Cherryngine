//package io.github.dockyardmc.protocol.packets.configurations
//
//import io.github.dockyardmc.extentions.readEnum
//import io.github.dockyardmc.extentions.readUUID
//import io.github.dockyardmc.protocol.packets.ServerboundPacket
//import io.github.dockyardmc.resourcepack.ResourcePack
//import io.netty.buffer.ByteBuf
//import java.util.*
//
//data class ServerboundConfigurationResourcepackResponsePacket(
//    var uuid: UUID,
//    var response: ResourcePack.Status
//) : ServerboundPacket {
//    companion object {
//        fun read(buffer: ByteBuf): ServerboundConfigurationResourcepackResponsePacket {
//            return ServerboundConfigurationResourcepackResponsePacket(buffer.readUUID(), buffer.readEnum<ResourcePack.Status>())
//        }
//    }
//}