package ru.cherryngine.lib.minecraft.dialog

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder

data class DialogButton(
    val label: Component,
    val tooltip: Component?,
    val width: Int,
    val action: ClickEvent?,
) {
    companion object {
        val ACTION_CODEC = object : Codec<ClickEvent> {
            override fun <D> encode(transcoder: Transcoder<D>, value: ClickEvent): D {
                TODO("Not yet implemented")
            }

            override fun <D> decode(transcoder: Transcoder<D>, value: D): ClickEvent {
                TODO("Not yet implemented")
            }

        }
        val CODEC = StructCodec.of(
            "label", ComponentCodec, DialogButton::label,
            "tooltip", ComponentCodec.optional(), DialogButton::tooltip,
            "width", Codec.INT, DialogButton::width,
            "action", ACTION_CODEC.optional(), DialogButton::action,
            ::DialogButton
        )
    }
}