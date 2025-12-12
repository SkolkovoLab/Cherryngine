package ru.cherryngine.lib.minecraft.registry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromStream
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.transcoder.KtJsonTranscoder

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
abstract class CodecDataDrivenRegistry<T : RegistryEntry>(
    identifier: String,
    resource: String,
    serializer: Codec<T>,
) : Registry<T>(identifier) {
    init {
        val resource = ClassLoader.getSystemResource(resource)
            ?: throw IllegalStateException("No resource file path for registry $identifier")
        val stream = resource.openStream()
        val parsed = Json.decodeFromStream<Map<String, JsonObject>>(stream)


        parsed.entries.forEachIndexed { index, (key, value) ->
            val entry = serializer.decode(KtJsonTranscoder, value)
            entry.setIdentifier(key)
            addEntry(entry)
        }
    }
}