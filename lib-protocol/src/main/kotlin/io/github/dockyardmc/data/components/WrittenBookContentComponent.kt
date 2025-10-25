package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

class WrittenBookContentComponent(
    val title: WritableBookContent.FilteredText,
    val author: String,
    val generation: Int,
    val pages: List<WritableBookContent.FilteredText>,
    val resolved: Boolean
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            WritableBookContent.FilteredText.STREAM_CODEC, WrittenBookContentComponent::title,
            StreamCodec.STRING, WrittenBookContentComponent::author,
            StreamCodec.VAR_INT, WrittenBookContentComponent::generation,
            WritableBookContent.FilteredText.STREAM_CODEC.list(), WrittenBookContentComponent::pages,
            StreamCodec.BOOLEAN, WrittenBookContentComponent::resolved,
            ::WrittenBookContentComponent
        )
    }
}