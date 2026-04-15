package ru.cherryngine.engine.bedrock.world

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import jakarta.inject.Singleton
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtType
import org.cloudburstmc.nbt.NbtUtils
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.world.block.BlockStates
import java.io.DataInputStream
import java.util.zip.GZIPInputStream

/**
 * Maps Java block stateIds to Bedrock runtime IDs.
 *
 * Loads the Bedrock block palette from resources and matches blocks by name.
 * States/properties are not matched — each Java block maps to the first
 * Bedrock palette entry with the same name (default state).
 */
@Singleton
class BedrockBlockMapping {
    private val log = LoggerFactory.getLogger(BedrockBlockMapping::class.java)

    /** javaStateId -> bedrockRuntimeId */
    private val mapping = Int2IntOpenHashMap().apply { defaultReturnValue(BEDROCK_AIR_RUNTIME_ID) }

    /** Bedrock block palette entries (index = runtime ID) */
    val paletteEntries: List<NbtMap>

    init {
        // Load Bedrock block palette
        val paletteStream = BedrockBlockMapping::class.java.getResourceAsStream("/bedrock/block_palette.nbt")
            ?: throw IllegalStateException("Missing bedrock/block_palette.nbt resource")

        val blockPalette: NbtMap = paletteStream.use { stream ->
            NbtUtils.createGZIPReader(stream).use { reader ->
                reader.readTag() as NbtMap
            }
        }

        val vanillaBlocks = blockPalette.getList("blocks", NbtType.COMPOUND)
        paletteEntries = vanillaBlocks.toList()

        // Build name -> first runtimeId index (default state per block name)
        val nameToRuntimeId = HashMap<String, Int>(vanillaBlocks.size)
        for (i in vanillaBlocks.indices) {
            val entry = vanillaBlocks[i]
            val name = entry.getString("name")
            nameToRuntimeId.putIfAbsent(name, i)
        }

        // Map each Java stateId -> Bedrock runtimeId by block name
        var mapped = 0
        var unmapped = 0
        for ((stateId, block) in BlockStates.blockStates) {
            val javaName = block.registryBlock.key.toString()
            val bedrockId = nameToRuntimeId[javaName]
            if (bedrockId != null) {
                mapping[stateId] = bedrockId
                mapped++
            } else {
                unmapped++
            }
        }

        log.info("Bedrock block mapping: {} palette entries, {} mapped, {} unmapped (fallback to air)",
            paletteEntries.size, mapped, unmapped)
    }

    fun getBedrockRuntimeId(javaStateId: Int): Int {
        return mapping.get(javaStateId)
    }

    companion object {
        /** Bedrock runtime ID for air — always 0 in the palette */
        const val BEDROCK_AIR_RUNTIME_ID = 0
    }
}
