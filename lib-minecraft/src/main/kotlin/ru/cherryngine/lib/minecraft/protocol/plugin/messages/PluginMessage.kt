package ru.cherryngine.lib.minecraft.protocol.plugin.messages

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

interface PluginMessage {
    data class Contents(
        val channel: String,
        val data: ByteArray,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Contents::channel,
                StreamCodec.RAW_BYTES_ARRAY, Contents::data,
                ::Contents
            )
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Contents

            if (channel != other.channel) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = channel.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }
}