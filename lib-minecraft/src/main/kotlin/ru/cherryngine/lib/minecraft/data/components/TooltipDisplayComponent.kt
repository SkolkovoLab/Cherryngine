package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.DataComponentRegistry
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.extentions.getOrThrow
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
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