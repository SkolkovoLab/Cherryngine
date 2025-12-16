package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class WolfVariant(
    val assets: Assets,
) {
    companion object {
        val CODEC = StructCodec.of(
            "assets", Assets.CODEC, WolfVariant::assets,
            ::WolfVariant
        )
    }

    data class Assets(
        val wild: Key,
        val tame: Key,
        val angry: Key,
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "wild", Codec.KEY, Assets::wild,
                "tame", Codec.KEY, Assets::tame,
                "angry", Codec.KEY, Assets::angry,
                ::Assets
            )
        }
    }
}