package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.utils.toKey

sealed interface DialogInput {
    val key: String
    val label: Component

    companion object {
        val CODEC = object : Codec<DialogInput> {
            override fun <D> encode(transcoder: Transcoder<D>, value: DialogInput): D {
                val (type: Key, codec: StructCodec<out DialogInput>) = when (value) {
                    is BooleanInput -> BooleanInput.KEY to BooleanInput.CODEC
                    is NumberRange -> NumberRange.KEY to NumberRange.CODEC
                    is SingleOptionDialogInput -> SingleOptionDialogInput.KEY to SingleOptionDialogInput.CODEC
                    is TextDialogInput -> TextDialogInput.KEY to TextDialogInput.CODEC
                }

                @Suppress("UNCHECKED_CAST")
                codec as StructCodec<DialogInput>

                return transcoder.encodeMap()
                    .put("type", Codec.KEY.encode(transcoder, type))
                    .also { codec.encodeToMap(transcoder, value, it) }
                    .build()
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): DialogInput {
                val map = transcoder.decodeMap(value)
                val type = Codec.KEY.decode(transcoder, map.getValue("type"))
                val codec = when (type) {
                    BooleanInput.KEY -> BooleanInput.CODEC
                    NumberRange.KEY -> NumberRange.CODEC
                    SingleOptionDialogInput.KEY -> SingleOptionDialogInput.CODEC
                    TextDialogInput.KEY -> TextDialogInput.CODEC
                    else -> throw IllegalArgumentException()
                }

                return codec.decode(transcoder, value)
            }
        }
    }

    data class BooleanInput(
        override val key: String,
        override val label: Component,
        val initial: Boolean,
        val onTrue: String,
        val onFalse: String,
    ) : DialogInput {
        companion object {
            val KEY = "boolean".toKey()
            val CODEC = StructCodec.of(
                "key", Codec.STRING, BooleanInput::key,
                "label", ComponentCodec, BooleanInput::label,
                "initial", Codec.BOOLEAN, BooleanInput::initial,
                "on_true", Codec.STRING, BooleanInput::onTrue,
                "on_false", Codec.STRING, BooleanInput::onFalse,
                ::BooleanInput
            )
        }
    }

    data class NumberRange(
        override val key: String,
        override val label: Component,
        val start: Double,
        val end: Double,
        val step: Float?,
        val width: Int,
        val initial: Double?,
        val labelFormat: String,
    ) : DialogInput {
        init {
            require(step == null || step > 0) { "step must be positive" }
            require(width in 1..1024) { "width must be between 1 and 1024 (inclusive)" }
        }

        companion object {
            val KEY = "number_range".toKey()
            val CODEC = StructCodec.of(
                "key", Codec.STRING, NumberRange::key,
                "label", ComponentCodec, NumberRange::label,
                "start", Codec.DOUBLE, NumberRange::start,
                "end", Codec.DOUBLE, NumberRange::end,
                "step", Codec.FLOAT.optional(), NumberRange::step,
                "width", Codec.INT, NumberRange::width,
                "initial", Codec.DOUBLE.optional(), NumberRange::initial,
                "label_format", Codec.STRING, NumberRange::labelFormat,
                ::NumberRange
            )
        }
    }

    data class SingleOptionDialogInput(
        override val key: String,
        override val label: Component,
        val options: List<Option>,
        val width: Int,
        val labelVisible: Boolean,
    ) : DialogInput {
        companion object {
            val KEY = "single_option".toKey()
            val CODEC = StructCodec.of(
                "key", Codec.STRING, SingleOptionDialogInput::key,
                "label", ComponentCodec, SingleOptionDialogInput::label,
                "options", Option.CODEC.list(), SingleOptionDialogInput::options,
                "width", Codec.INT, SingleOptionDialogInput::width,
                "label_visible", Codec.BOOLEAN, SingleOptionDialogInput::labelVisible,
                ::SingleOptionDialogInput
            )
        }

        data class Option(
            val id: String,
            val label: Component,
            val initial: Boolean,
        ) {
            companion object {
                val CODEC = StructCodec.of(
                    "id", Codec.STRING, Option::id,
                    "label", ComponentCodec, Option::label,
                    "initial", Codec.BOOLEAN, Option::initial,
                    ::Option
                )
            }
        }
    }

    data class TextDialogInput(
        override val key: String,
        override val label: Component,
        val width: Int,
        val labelVisible: Boolean,
        val initial: String,
        val maxLength: Int,
        val multiline: Multiline?,
    ) : DialogInput {
        companion object {
            val KEY = "text".toKey()
            val CODEC = StructCodec.of(
                "key", Codec.STRING, TextDialogInput::key,
                "label", ComponentCodec, TextDialogInput::label,
                "width", Codec.INT, TextDialogInput::width,
                "label_visible", Codec.BOOLEAN, TextDialogInput::labelVisible,
                "initial", Codec.STRING, TextDialogInput::initial,
                "max_length", Codec.INT, TextDialogInput::maxLength,
                "multiline", Multiline.CODEC.optional(), TextDialogInput::multiline,
                ::TextDialogInput
            )
        }

        data class Multiline(
            val maxLines: Int?,
            val height: Int?,
        ) {
            init {
                require(maxLines == null || maxLines > 0) { "maxLines must be positive" }
                require(height == null || height > 0) { "height must be positive" }
            }

            companion object {
                val CODEC = StructCodec.of(
                    "max_lines", Codec.INT.optional(), Multiline::maxLines,
                    "height", Codec.INT.optional(), Multiline::height,
                    ::Multiline
                )
            }
        }
    }
}