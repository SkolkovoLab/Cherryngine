package ru.cherryngine.engine.bedrock.world

import com.google.gson.JsonParser
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import jakarta.inject.Singleton
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtType
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.world.block.BlockStates

@Singleton
class BedrockBlockMapping {
    private val log = LoggerFactory.getLogger(BedrockBlockMapping::class.java)

    /** javaStateId -> bedrockRuntimeId */
    private val blockMapping = Int2IntOpenHashMap().apply { defaultReturnValue(BEDROCK_AIR_RUNTIME_ID) }

    /** Bedrock block palette entries (index = runtime ID) */
    val paletteEntries: List<NbtMap>

    /** item name -> item runtime ID */
    private val itemRuntimeIds = HashMap<String, Int>()

    init {
        // Block palette
        val paletteStream = BedrockBlockMapping::class.java.getResourceAsStream("/bedrock/block_palette.nbt")
            ?: throw IllegalStateException("Missing bedrock/block_palette.nbt resource")
        val blockPalette: NbtMap = paletteStream.use { stream ->
            NbtUtils.createGZIPReader(stream).use { it.readTag() as NbtMap }
        }
        val vanillaBlocks = blockPalette.getList("blocks", NbtType.COMPOUND)
        paletteEntries = vanillaBlocks.toList()

        val nameToRuntimeId = HashMap<String, Int>(vanillaBlocks.size)
        for (i in vanillaBlocks.indices) {
            nameToRuntimeId.putIfAbsent(vanillaBlocks[i].getString("name"), i)
        }

        var mapped = 0
        var unmapped = 0
        for ((stateId, block) in BlockStates.blockStates) {
            val bedrockId = nameToRuntimeId[block.registryBlock.key.toString()]
            if (bedrockId != null) {
                blockMapping[stateId] = bedrockId
                mapped++
            } else {
                unmapped++
            }
        }
        log.info("Bedrock block mapping: {} palette entries, {} mapped, {} unmapped",
            paletteEntries.size, mapped, unmapped)

        // Item runtime states
        val itemStream = BedrockBlockMapping::class.java.getResourceAsStream("/bedrock/runtime_item_states.json")
        if (itemStream != null) {
            val items = JsonParser.parseReader(itemStream.reader()).asJsonArray
            for (item in items) {
                val obj = item.asJsonObject
                itemRuntimeIds[obj["name"].asString] = obj["id"].asInt
            }
            log.info("Bedrock item mapping: {} items loaded", itemRuntimeIds.size)
        }
    }

    fun getBedrockRuntimeId(javaStateId: Int): Int = blockMapping.get(javaStateId)

    /** Create ItemData for a block/item by name (e.g. "minecraft:tnt") */
    fun createItemData(name: String): ItemData {
        val runtimeId = itemRuntimeIds[name] ?: 1
        return ItemData.builder()
            .definition(SimpleItemDefinition(name, runtimeId, false))
            .count(1)
            .build()
    }

    companion object {
        const val BEDROCK_AIR_RUNTIME_ID = 0
    }
}
