package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

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