package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.utils.toKey

sealed interface DialogBody {
    companion object {
        val CODEC = object : Codec<DialogBody> {
            override fun <D> encode(transcoder: Transcoder<D>, value: DialogBody): D {
                val (type: Key, codec: StructCodec<out DialogBody>) = when (value) {
                    is Item -> Item.KEY to Item.CODEC
                    is PlainMessage -> PlainMessage.KEY to PlainMessage.CODEC
                }

                @Suppress("UNCHECKED_CAST")
                codec as StructCodec<DialogBody>

                return transcoder.encodeMap()
                    .put("type", Codec.KEY.encode(transcoder, type))
                    .also { codec.encodeToMap(transcoder, value, it) }
                    .build()
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): DialogBody {
                val map = transcoder.decodeMap(value)
                val type = Codec.KEY.decode(transcoder, map.getValue("type"))
                val codec = when (type) {
                    Item.KEY -> Item.CODEC
                    PlainMessage.KEY -> PlainMessage.CODEC
                    else -> throw IllegalArgumentException()
                }

                return codec.decode(transcoder, value)
            }
        }
    }

    data class Item(
        val item: ItemStack,
        val description: PlainMessage?,
        val showDecorations: Boolean,
        val showTooltip: Boolean,
        val width: Int,
        val height: Int,
    ) : DialogBody {
        init {
            require(width in 1..256) { "width must be between 1 and 256 (inclusive)" }
            require(height in 1..256) { "height must be between 1 and 256 (inclusive)" }
        }

        companion object {
            val KEY = "item".toKey()
            val CODEC = StructCodec.of(
                "item", ItemStack.CODEC, Item::item,
                "description", PlainMessage.CODEC.optional(), Item::description,
                "show_decorations", Codec.BOOLEAN, Item::showDecorations,
                "show_tooltip", Codec.BOOLEAN, Item::showTooltip,
                "width", Codec.INT, Item::width,
                "height", Codec.INT, Item::height,
                ::Item
            )
        }
    }

    data class PlainMessage(
        val content: Component,
        val width: Int = 200,
    ) : DialogBody {
        companion object {
            val KEY = "plain_message".toKey()
            val CODEC = StructCodec.of(
                "content", ComponentCodec, PlainMessage::content,
                "width", Codec.INT, PlainMessage::width,
                ::PlainMessage
            )
        }
    }
}