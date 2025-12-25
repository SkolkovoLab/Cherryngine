package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ItemModelComponent(
    val itemModel: String
) : DataComponent() {

    companion object {
        val CODEC = Codec.STRING.transform(
            ::ItemModelComponent,
            ItemModelComponent::itemModel
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ItemModelComponent::itemModel,
            ::ItemModelComponent
        )
    }
}