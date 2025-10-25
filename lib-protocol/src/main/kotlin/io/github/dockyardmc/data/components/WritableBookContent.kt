package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

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