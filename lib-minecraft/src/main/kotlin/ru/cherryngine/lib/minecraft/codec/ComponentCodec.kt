package ru.cherryngine.lib.minecraft.codec

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.KtJsonTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder

object ComponentCodec : Codec<Component> {
    override fun <D> encode(
        transcoder: Transcoder<D>,
        value: Component,
    ): D {
        when (transcoder) {
            is KtJsonTranscoder -> {
                val jsonString = JSONComponentSerializer.json().serialize(value)
                val jsonElement = Json.parseToJsonElement(jsonString)
                @Suppress("UNCHECKED_CAST")
                return jsonElement as D
            }

            is BinaryTagTranscoder -> {
                val binaryTag = NBTComponentSerializer.nbt().serialize(value)
                @Suppress("UNCHECKED_CAST")
                return binaryTag as D
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    override fun <D> decode(
        transcoder: Transcoder<D>,
        value: D,
    ): Component {
        return when (transcoder) {
            is KtJsonTranscoder -> {
                value as JsonElement
                val jsonString = Json.encodeToString<JsonElement>(value)
                JSONComponentSerializer.json().deserialize(jsonString)
            }

            is BinaryTagTranscoder -> {
                value as BinaryTag
                NBTComponentSerializer.nbt().deserialize(value)
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }
}