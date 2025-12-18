package ru.cherryngine.lib.minecraft.codec

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.nbt.NBTComponentSerializer
import ru.cherryngine.lib.minecraft.codec.transcoder.GsonTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.KtJsonTranscoder
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import com.google.gson.JsonElement as GsonElement

object ComponentCodec : Codec<Component> {
    override fun <D> encode(
        transcoder: Transcoder<D>,
        value: Component,
    ): D {
        if (transcoder is KtJsonTranscoder) {
            val jsonString = JSONComponentSerializer.json().serialize(value)
            val jsonElement = Json.parseToJsonElement(jsonString)
            @Suppress("UNCHECKED_CAST")
            return jsonElement as D
        }

        if (transcoder is GsonTranscoder) {
            val gsonElement = GsonComponentSerializer.gson().serializeToTree(value)
            @Suppress("UNCHECKED_CAST")
            return gsonElement as D
        }

        val binaryTag = NBTComponentSerializer.nbt().serialize(value)
        return BinaryTagCodec.CODEC.encode(transcoder, binaryTag)
    }

    override fun <D> decode(
        transcoder: Transcoder<D>,
        value: D,
    ): Component {
        if (transcoder is KtJsonTranscoder) {
            value as JsonElement
            val jsonString = Json.encodeToString<JsonElement>(value)
            return JSONComponentSerializer.json().deserialize(jsonString)
        }
        if (transcoder is GsonTranscoder) {
            value as GsonElement
            return GsonComponentSerializer.gson().deserializeFromTree(value)
        }

        val binaryTag = BinaryTagCodec.CODEC.decode(transcoder, value)
        return NBTComponentSerializer.nbt().deserialize(binaryTag)
    }
}