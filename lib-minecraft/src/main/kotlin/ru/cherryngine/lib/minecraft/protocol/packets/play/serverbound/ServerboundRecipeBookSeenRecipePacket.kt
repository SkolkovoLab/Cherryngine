package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundRecipeBookSeenRecipePacket(
    val recipeId: Int,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundRecipeBookSeenRecipePacket::recipeId,
            ::ServerboundRecipeBookSeenRecipePacket
        )
    }
}