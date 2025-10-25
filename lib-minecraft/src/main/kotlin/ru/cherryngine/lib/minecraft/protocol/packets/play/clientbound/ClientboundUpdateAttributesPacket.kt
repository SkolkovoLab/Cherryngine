//package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound
//
//import ru.cherryngine.lib.minecraft.attributes.AttributeModifier
//import ru.cherryngine.lib.minecraft.entity.Entity
//import ru.cherryngine.lib.minecraft.extentions.readList
//import ru.cherryngine.lib.minecraft.extentions.readVarInt
//import ru.cherryngine.lib.minecraft.extentions.writeVarInt
//import ru.cherryngine.lib.minecraft.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.protocol.NetworkWritable
//import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
//import ru.cherryngine.lib.minecraft.protocol.types.writeList
//import ru.cherryngine.lib.minecraft.registry.registries.Attribute
//import ru.cherryngine.lib.minecraft.registry.registries.AttributeRegistry
//import io.netty.buffer.ByteBuf
//
//class ClientboundUpdateAttributesPacket(val entity: Int, val properties: Collection<Property>) : ClientboundPacket() {
//
//    init {
//        buffer.writeVarInt(entity)
//        buffer.writeList(properties, Property::write)
//    }
//
//    data class Property(val attribute: Attribute, val value: Double, val modifiers: Collection<AttributeModifier>) : NetworkWritable {
//
//        override fun write(buffer: ByteBuf) {
//            buffer.writeVarInt(attribute.getProtocolId())
//            buffer.writeDouble(value)
//            buffer.writeList(modifiers, AttributeModifier::write)
//        }
//
//        companion object : NetworkReadable<Property> {
//
//            override fun read(buffer: ByteBuf): Property {
//                return Property(
//                    attribute = AttributeRegistry.getByProtocolId(buffer.readVarInt()),
//                    value = buffer.readDouble(),
//                    modifiers = buffer.readList { b -> AttributeModifier.STREAM_CODEC.read(b) }
//                )
//            }
//        }
//    }
//}