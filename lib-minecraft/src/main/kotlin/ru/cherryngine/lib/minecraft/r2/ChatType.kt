package ru.cherryngine.lib.minecraft.r2

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class ChatType(
    val chat: Decoration,
    val narration: Decoration,
) {
    companion object {
        val CODEC = StructCodec.of(
            "chat", Decoration.CODEC, ChatType::chat,
            "narration", Decoration.CODEC, ChatType::narration,
            ::ChatType
        )
    }

    data class Decoration(
        val translationKey: String,
        val parameters: List<String>,
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "translation_key", Codec.STRING, Decoration::translationKey,
                "parameters", Codec.STRING.list(), Decoration::parameters,
                ::Decoration
            )
        }
    }
}