//package ru.cherryngine.lib.minecraft.data.components
//
//import ru.cherryngine.lib.minecraft.data.DataComponent
//import ru.cherryngine.lib.minecraft.data.HashHolder
//import ru.cherryngine.lib.minecraft.item.Enchantment
//import ru.cherryngine.lib.minecraft.protocol.NetworkReadable
//import ru.cherryngine.lib.minecraft.protocol.types.readList
//import ru.cherryngine.lib.minecraft.protocol.types.writeList
//import io.netty.buffer.ByteBuf
//
//class EnchantmentsComponent(val list: List<Enchantment>) : DataComponent() {
//
//    override fun write(buffer: ByteBuf) {
//        buffer.writeList(list, Enchantment::write)
//    }
//
//    //TODO(Enchantments)
//    override fun hashStruct(): HashHolder {
//        return unsupported(this::class)
//    }
//
//    companion object : NetworkReadable<EnchantmentsComponent> {
//        override fun read(buffer: ByteBuf): EnchantmentsComponent {
//            return EnchantmentsComponent(buffer.readList(Enchantment::read))
//        }
//    }
//}