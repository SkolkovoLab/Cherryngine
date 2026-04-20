package ru.cherryngine.engine.bedrock.world

import com.google.gson.JsonParser
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import jakarta.inject.Singleton
import net.minestom.server.instance.block.Block
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtType
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData
import org.slf4j.LoggerFactory

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
        for (blockType in Block.values()) {
            val bedrockId = nameToRuntimeId[blockType.key().asString()]
            if (bedrockId == null) {
                unmapped += blockType.possibleStates().size
                continue
            }
            for (state in blockType.possibleStates()) {
                blockMapping[state.stateId()] = bedrockId
                mapped++
            }
        }
        log.info("Bedrock block mapping: {} palette entries, {} mapped, {} unmapped",
            paletteEntries.size, mapped, unmapped)

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
