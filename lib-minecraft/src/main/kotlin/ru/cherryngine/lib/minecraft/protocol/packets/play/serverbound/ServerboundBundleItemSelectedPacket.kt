package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class ServerboundBundleItemSelectedPacket(
    val slotOfBundle: Int,
    val slotInBundle: Int,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundBundleItemSelectedPacket::slotOfBundle,
            StreamCodec.VAR_INT, ServerboundBundleItemSelectedPacket::slotInBundle,
            ::ServerboundBundleItemSelectedPacket
        )
    }
}