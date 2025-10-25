package ru.cherryngine.lib.minecraft.protocol.plugin.messages

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class UnregisterPluginMessage(
    val channels: List<String>
) : PluginMessage {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegisterPluginMessage.MOJANG_FUCKED_STRING_LIST_STREAM_CODEC, UnregisterPluginMessage::channels,
            ::UnregisterPluginMessage
        )
    }
}
