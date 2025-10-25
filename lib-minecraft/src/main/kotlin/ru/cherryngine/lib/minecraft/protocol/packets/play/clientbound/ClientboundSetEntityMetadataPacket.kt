//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.entity.Entity
//import ru.cherryngine.lib.minecraft.entity.metadata.EntityMetadata
//import ru.cherryngine.lib.minecraft.entity.metadata.writeMetadata
//import ru.cherryngine.lib.minecraft.extentions.writeVarInt
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//
//class ClientboundSetEntityMetadataPacket(entity: Entity, metadata: Collection<EntityMetadata>) : ClientboundPacket() {
//
//    init {
//        buffer.writeVarInt(entity.id)
//        metadata.forEach {
//            buffer.writeMetadata(it)
//        }
//        // array end byte
//        buffer.writeByte(0xFF)
//    }
//}