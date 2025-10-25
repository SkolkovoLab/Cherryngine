package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.DataComponentRegistry
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.extentions.getOrThrow
import io.github.dockyardmc.tide.stream.StreamCodec
import kotlin.reflect.KClass

class TooltipDisplayComponent(
    val hideTooltip: Boolean,
    val hiddenComponents: List<KClass<out DataComponent>>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            default("hide_tooltip", false, hideTooltip, CRC32CHasher::ofBoolean)
            defaultList("hidden_components", listOf(), hiddenComponents.map { component -> DataComponentRegistry.dataComponentsByIdentifierReversed.getOrThrow(component) }, CRC32CHasher::ofString)
        }
    }

    companion object {
        private val kClassStreamCodec = StreamCodec.VAR_INT.transform(
            { int -> DataComponentRegistry.dataComponentsById.getValue(int) },
            { kClass -> DataComponentRegistry.dataComponentsByIdReversed.getValue(kClass) }
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, TooltipDisplayComponent::hideTooltip,
            kClassStreamCodec.list(), TooltipDisplayComponent::hiddenComponents,
            ::TooltipDisplayComponent
        )
    }
}