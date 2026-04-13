package ru.cherryngine.engine.physics.terrain

import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.world.LayerEntry

data class LayerWithContext(
    val entry: LayerEntry,
    val contextIDs: Set<String>,
    val dimensionType: DimensionType,
)