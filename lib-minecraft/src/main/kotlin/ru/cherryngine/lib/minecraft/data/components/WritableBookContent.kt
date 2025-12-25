package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class WritableBookContent(
    val pages: List<FilteredText>,
) : DataComponent() {

    companion object {
        val EMPTY = WritableBookContent(listOf())

        val CODEC = FilteredText.CODEC.list().transform(
            ::WritableBookContent,
            WritableBookContent::pages
        )

        val STREAM_CODEC = StreamCodec.of(
            FilteredText.STREAM_CODEC.list(), WritableBookContent::pages,
            ::WritableBookContent
        )
    }

    data class FilteredText(
        val text: String,
        val filtered: String? = null,
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "text", Codec.STRING, FilteredText::text,
                "filtered", Codec.STRING.optional(), FilteredText::filtered,
                ::FilteredText
            )

            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, FilteredText::text,
                StreamCodec.STRING.optional(), FilteredText::filtered,
                ::FilteredText
            )
        }
    }
}