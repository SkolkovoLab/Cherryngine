package ru.cherryngine.lib.minecraft.registry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.util.zip.GZIPInputStream

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
abstract class DataDrivenRegistry<T : RegistryEntry>(
    identifier: String,
    resource: String,
    serializer: KSerializer<T>
) : Registry<T>(identifier) {

    init {
        val resource = ClassLoader.getSystemResource(resource)
            ?: throw IllegalStateException("No resource file path for registry $identifier")
        val stream = GZIPInputStream(resource.openStream())
        val list = Json.decodeFromStream(ListSerializer(serializer), stream)
        list.forEach(::addEntry)
    }
}