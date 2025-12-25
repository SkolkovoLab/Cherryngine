package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.DataComponentRegistry
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import kotlin.reflect.KClass

class TooltipDisplayComponent(
    val hideTooltip: Boolean,
    val hiddenComponents: List<KClass<out DataComponent>>
) : DataComponent() {

    companion object {
        private val kClassStreamCodec: StreamCodec<KClass<out DataComponent>> = StreamCodec.VAR_INT.transform(
            { int -> DataComponentRegistry.registry[int].value.type },
            { kClass -> DataComponentRegistry.get(kClass).id }
        )

        val CODEC = StructCodec.of(
            "hide_tooltip",
            Codec.BOOLEAN.default(false),
            TooltipDisplayComponent::hideTooltip,

            "hidden_components",
            Codec.KEY.list()
                .default(emptyList())
                .transform(
                    { ids ->
                        ids.map { id ->
                            DataComponentRegistry.registry[id].value.type
                        }
                    },
                    { classes ->
                        classes.map { cls ->
                            DataComponentRegistry.get(cls).key
                        }
                    }
                ),
            TooltipDisplayComponent::hiddenComponents,

            ::TooltipDisplayComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.BOOLEAN, TooltipDisplayComponent::hideTooltip,
            kClassStreamCodec.list(), TooltipDisplayComponent::hiddenComponents,
            ::TooltipDisplayComponent
        )
    }
}