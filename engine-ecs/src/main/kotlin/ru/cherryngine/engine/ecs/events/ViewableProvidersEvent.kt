package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.minecraft.view.StaticViewableProvider
import ru.cherryngine.engine.minecraft.view.ViewableProvider
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.world.LayerEntry

class ViewableProvidersEvent(
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf(),
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf(),
    val layers: MutableList<LayerEntry> = mutableListOf(),
    var dimensionType: DimensionType? = null,
) : EcsEvent<ViewableProvidersEvent> {
    override fun type() = ViewableProvidersEvent

    companion object : ComponentType<ViewableProvidersEvent>()
}
