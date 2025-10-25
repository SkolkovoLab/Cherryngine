//package ru.cherryngine.lib.minecraft.protocol.packets.configurations
//
//import ru.cherryngine.lib.minecraft.extentions.readEnum
//import ru.cherryngine.lib.minecraft.extentions.readUUID
//import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
//import ru.cherryngine.lib.minecraft.resourcepack.ResourcePack
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