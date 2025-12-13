package ru.cherryngine.lib.minecraft.registry

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.*
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
abstract class KtJsonDataDrivenRegistry<T : RegistryEntry>(
    identifier: String,
    resource: String,
    serializer: KSerializer<T>,
) : Registry<T>(identifier) {
    init {
        val resource = ClassLoader.getSystemResource(resource)
            ?: throw IllegalStateException("No resource file path for registry $identifier")
        val stream = resource.openStream()
        val parsed = Json.decodeFromStream<Map<String, JsonObject>>(stream)

        parsed.entries.forEachIndexed { index, (key, value) ->
            val obj = value.toMutableMap()
            if ("id" in obj && this !is BlockRegistry) {
                val removed = obj.remove("id")
                val id = removed!!.jsonPrimitive.int
                check(id == index)
            }
            obj["identifier"] = JsonPrimitive(key)
            val entry = Json.decodeFromJsonElement(serializer, JsonObject(obj))
            addEntry(entry)
        }
    }
}