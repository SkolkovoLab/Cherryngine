package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundRecipeBookChangeSettingsPacket(
    val bookType: BookType,
    val bookOpen: Boolean,
    val filterActive: Boolean,
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<BookType>(), ServerboundRecipeBookChangeSettingsPacket::bookType,
            StreamCodec.BOOLEAN, ServerboundRecipeBookChangeSettingsPacket::bookOpen,
            StreamCodec.BOOLEAN, ServerboundRecipeBookChangeSettingsPacket::filterActive,
            ::ServerboundRecipeBookChangeSettingsPacket
        )
    }

    enum class BookType {
        CRAFTING,
        FURNACE,
        BLAST_FURNACE,
        SMOKER
    }
}