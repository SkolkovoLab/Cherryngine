package ru.cherryngine.engine.core.world

import ru.cherryngine.lib.world.ImmutableLayer
import ru.cherryngine.lib.world.ImmutableLayerKey
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayer

/**
 * Разделение слоёв на immutable и mutable с предвычисленным ключом для ChunkPool.
 * Вычисляется один раз на игрока за тик.
 */
data class LayerClassification(
    val immutableLayers: List<LayerEntry>,
    val mutableLayers: List<LayerEntry>,
    val allLayersSorted: List<LayerEntry>,
    val immutableKey: ImmutableLayerKey,
) {
    companion object {
        fun classify(layers: List<LayerEntry>): LayerClassification {
            val sorted = layers.sortedByDescending { it.priority }
            val immutable = sorted.filter { it.layer is ImmutableLayer }
            val mutable = sorted.filter { it.layer is MutableLayer }
            return LayerClassification(
                immutableLayers = immutable,
                mutableLayers = mutable,
                allLayersSorted = sorted,
                immutableKey = ImmutableLayerKey.from(immutable),
            )
        }
    }
}
