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
//class StoredEnchantmentsComponent(val enchantments: List<Enchantment>) : DataComponent() {
//
//    override fun write(buffer: ByteBuf) {
//        buffer.writeList(enchantments, Enchantment::write)
//    }
//
//    override fun hashStruct(): HashHolder {
//        return unsupported(this)
//    }
//
//    companion object : NetworkReadable<StoredEnchantmentsComponent> {
//        override fun read(buffer: ByteBuf): StoredEnchantmentsComponent {
//            return StoredEnchantmentsComponent(buffer.readList(Enchantment::read))
//        }
//    }
//
//}