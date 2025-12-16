package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec


data class ZombieNautilusVariant(
    val model: Model,
    val assetId: Key,
) {
    companion object {
        val CODEC = StructCodec.of(
            "model", Model.CODEC.default(Model.NORMAL), ZombieNautilusVariant::model,
            "asset_id", Codec.KEY, ZombieNautilusVariant::assetId,
            ::ZombieNautilusVariant
        )
    }

    enum class Model {
        NORMAL,
        WARM;

        companion object {
            val CODEC = Codec.enum<Model>()
        }
    }

}