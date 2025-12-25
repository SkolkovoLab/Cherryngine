package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class WrittenBookContentComponent(
    val title: WritableBookContent.FilteredText,
    val author: String,
    val generation: Int,
    val pages: List<WritableBookContent.FilteredText>,
    val resolved: Boolean
) : DataComponent() {
    companion object {
        val CODEC = StructCodec.of(
            "title", WritableBookContent.FilteredText.CODEC, WrittenBookContentComponent::title,
            "author", Codec.STRING, WrittenBookContentComponent::author,
            "generation", Codec.INT, WrittenBookContentComponent::generation,
            "pages", WritableBookContent.FilteredText.CODEC.list(), WrittenBookContentComponent::pages,
            "resolved", Codec.BOOLEAN, WrittenBookContentComponent::resolved,
            ::WrittenBookContentComponent
        )

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