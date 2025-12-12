package ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundSetCreativeModeSlotPacket(
    val slot: Int,
    val clickedItem: ItemStack
) : ServerboundPacket {
    companion object {
        private val itemStackCodec: StreamCodec<ItemStack> = ItemStack.streamCodec(true, false)
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.INT_SHORT, ServerboundSetCreativeModeSlotPacket::slot,
            itemStackCodec, ServerboundSetCreativeModeSlotPacket::clickedItem,
            ::ServerboundSetCreativeModeSlotPacket
        )
    }
}