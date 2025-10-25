package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class WritableBookContent(
    val pages: List<FilteredText>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val EMPTY = WritableBookContent(listOf())

        val STREAM_CODEC = StreamCodec.of(
            FilteredText.STREAM_CODEC.list(), WritableBookContent::pages,
            ::WritableBookContent
        )
    }

    data class FilteredText(
        val text: String,
        val filtered: String? = null
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, FilteredText::text,
                StreamCodec.STRING.optional(), FilteredText::filtered,
                ::FilteredText
            )
        }
    }
}