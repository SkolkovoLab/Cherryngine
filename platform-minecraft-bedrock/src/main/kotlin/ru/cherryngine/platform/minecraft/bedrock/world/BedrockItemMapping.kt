package ru.cherryngine.platform.minecraft.bedrock.world

import com.google.gson.JsonParser
import jakarta.inject.Singleton
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.slf4j.LoggerFactory

@Singleton
class BedrockItemMapping {
    private val log = LoggerFactory.getLogger(BedrockItemMapping::class.java)

    /** item name -> item runtime ID */
    private val itemRuntimeIds = HashMap<String, Int>()

    init {
        val itemStream = BedrockItemMapping::class.java.getResourceAsStream("/bedrock/runtime_item_states.json")
        if (itemStream != null) {
            val items = JsonParser.parseReader(itemStream.reader()).asJsonArray
            for (item in items) {
                val obj = item.asJsonObject
                itemRuntimeIds[obj["name"].asString] = obj["id"].asInt
            }
            log.info("Bedrock item mapping: {} items loaded", itemRuntimeIds.size)
        }
    }

    fun createItemData(name: String): ItemData {
        val runtimeId = itemRuntimeIds[name] ?: 1
        return ItemData.builder()
            .definition(SimpleItemDefinition(name, runtimeId, false))
            .count(1)
            .build()
    }
}
