package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class RecipesComponent(
    val recipes: List<String>
) : DataComponent() {

    companion object {
        val CODEC = Codec.STRING.list().transform(
            ::RecipesComponent,
            RecipesComponent::recipes
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING.list(), RecipesComponent::recipes,
            ::RecipesComponent
        )
    }
}