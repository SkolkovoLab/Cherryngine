package ru.cherryngine.platform.minecraft.java.world

/**
 * Ключ, идентифицирующий упорядоченный набор immutable слоёв.
 * Порядок списка = порядок приоритета (сверху вниз).
 */
data class ImmutableLayerKey(
    val layerIds: List<String>,
) {
    companion object {
        fun from(layers: List<LayerEntry>): ImmutableLayerKey {
            return ImmutableLayerKey(layers.sortedByDescending { it.priority }.map { it.layer.id })
        }
    }
}
