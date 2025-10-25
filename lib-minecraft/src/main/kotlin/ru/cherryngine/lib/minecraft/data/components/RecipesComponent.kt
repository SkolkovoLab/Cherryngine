package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class RecipesComponent(
    val recipes: List<String>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(recipes.map { recipe -> CRC32CHasher.ofString(recipe) }))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING.list(), RecipesComponent::recipes,
            ::RecipesComponent
        )
    }
}