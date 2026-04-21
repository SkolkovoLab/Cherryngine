package ru.cherryngine.platform.minecraft.bedrock.world

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import jakarta.inject.Singleton
import net.minestom.server.instance.block.Block
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtType
import org.cloudburstmc.nbt.NbtUtils
import org.slf4j.LoggerFactory

@Singleton
class JavaToBedrockBlockMapping {
    private val log = LoggerFactory.getLogger(JavaToBedrockBlockMapping::class.java)

    /** javaStateId -> bedrockRuntimeId */
    private val blockMapping = Int2IntOpenHashMap().apply { defaultReturnValue(BEDROCK_AIR_RUNTIME_ID) }

    init {
        val paletteStream = JavaToBedrockBlockMapping::class.java.getResourceAsStream("/bedrock/block_palette.nbt")
            ?: throw IllegalStateException("Missing bedrock/block_palette.nbt resource")
        val blockPalette: NbtMap = paletteStream.use { stream ->
            NbtUtils.createGZIPReader(stream).use { it.readTag() as NbtMap }
        }
        val vanillaBlocks = blockPalette.getList("blocks", NbtType.COMPOUND)

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
            vanillaBlocks.size, mapped, unmapped)
    }

    fun getBedrockRuntimeId(javaStateId: Int): Int = blockMapping.get(javaStateId)

    companion object {
        const val BEDROCK_AIR_RUNTIME_ID = 0
    }
}
