package ru.cherryngine.engine.mcprotocollib

import ru.cherryngine.lib.world.ImmutableLayer
import ru.cherryngine.lib.world.ImmutableLayerKey
import ru.cherryngine.lib.world.LayerEntry
import ru.cherryngine.lib.world.MutableLayer

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
