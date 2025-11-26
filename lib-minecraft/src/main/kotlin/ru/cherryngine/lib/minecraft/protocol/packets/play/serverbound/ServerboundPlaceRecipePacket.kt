package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundPlaceRecipePacket(
    val windowId: Int,
    val recipeId: Int,
    val makeAll: Boolean, // Affects the amount of items processed; true if shift is down when clicked.
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundPlaceRecipePacket::windowId,
            StreamCodec.VAR_INT, ServerboundPlaceRecipePacket::recipeId,
            StreamCodec.BOOLEAN, ServerboundPlaceRecipePacket::makeAll,
            ::ServerboundPlaceRecipePacket
        )
    }
}