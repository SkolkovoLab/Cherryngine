//package ru.cherryngine.lib.minecraft.data.components
//
//import ru.cherryngine.lib.minecraft.attributes.Modifier
//import ru.cherryngine.lib.minecraft.data.DataComponent
//import ru.cherryngine.lib.minecraft.data.HashHolder
//import ru.cherryngine.lib.minecraft.data.HashList
//import ru.cherryngine.lib.minecraft.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.protocol.types.readList
//import ru.cherryngine.lib.minecraft.protocol.types.writeList
//import io.netty.buffer.ByteBuf
//
//class AttributeModifiersComponent(val attributes: List<Modifier>) : DataComponent(true) {
//
//    override fun write(buffer: ByteBuf) {
//        buffer.writeList(attributes, Modifier::write)
//    }
//
//    override fun hashStruct(): HashHolder {
//        return HashList(attributes.map { attribute -> attribute.hashStruct() })
//    }
//
//    companion object : NetworkReadable<AttributeModifiersComponent> {
//
//        override fun read(buffer: ByteBuf): AttributeModifiersComponent {
//            return AttributeModifiersComponent(buffer.readList(Modifier::read))
//        }
//    }
//}