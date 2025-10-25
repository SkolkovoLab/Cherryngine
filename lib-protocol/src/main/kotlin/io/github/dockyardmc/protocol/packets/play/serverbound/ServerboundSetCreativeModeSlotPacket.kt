package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

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