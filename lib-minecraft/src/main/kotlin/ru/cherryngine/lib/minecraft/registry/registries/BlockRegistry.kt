package ru.cherryngine.lib.minecraft.registry.registries

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.extentions.reversed
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.registry.RegistryException
import ru.cherryngine.lib.minecraft.world.block.Block

object BlockRegistry : DataDrivenRegistry<RegistryBlock>() {
    override val identifier: String = "minecraft:block"
    val STREAM_CODEC = RegistryStreamCodec(this)

    val AIR get() = BlockRegistry["minecraft:air"]
    val blockStates = Int2ObjectOpenHashMap<Block>()

    override fun addEntry(entry: RegistryBlock) {
        super.addEntry(entry)

        if (entry.states.isEmpty()) return
        entry.possibleStates.forEach { (state, id) ->
            val block = Block.getBlockFromStateStringFast(state, entry)
            blockStates.put(id, block)
        }
    }

    override fun getByProtocolId(id: Int): RegistryBlock {
        return super.getByProtocolIdOrNull(id) ?: blockStates.getOrDefault(id, null)?.registryBlock
        ?: throw RegistryException(id, entries.size)
    }

}

@Serializable
data class RegistryBlock(
    val identifier: String,
    val displayName: String,
    val explosionResistance: Float,
    val destroyTime: Float,
    val isSignalSource: Boolean,
    val lightEmission: Int,
    val isBlockEntity: Boolean,
    val blockEntityId: Int?,
    val lightFilter: Int,
    val isAir: Boolean,
    val isSolid: Boolean,
    val isLiquid: Boolean,
    val isFlammable: Boolean,
    val breakSpeed: Float,
    val requiresToolToBreak: Boolean,
    val canOcclude: Boolean,
    val replaceable: Boolean,
    val states: List<RegistryBlockState>,
    val defaultBlockStateId: Int,
    val minBlockStateId: Int,
    val maxBlockStateId: Int,
    val sounds: RegistryBlockSounds,
    val tags: List<String>,
    val possibleStates: Map<String, Int>,
    val shape: Map<Int, String>,
    val collisionShape: Map<Int, String>,
    val interactionShape: Map<Int, String>,
    val occlusionShape: Map<Int, String>,
    val visualShape: Map<Int, String>,
) : RegistryEntry {

    override fun getEntryIdentifier(): String {
        return identifier
    }

    @Contextual
    val possibleStatesReversed = Int2ObjectOpenHashMap(possibleStates.reversed())

    override fun getProtocolId(): Int {
        return defaultBlockStateId
    }

    fun getLegacyProtocolId(): Int {
        return BlockRegistry.getProtocolIdByEntry(this)
    }

    fun toItem(): Item {
        return ItemRegistry[identifier]
    }

    fun toBlock(): Block {
        return Block(this)
    }

    fun withBlockStates(vararg states: Pair<String, String>): Block {
        return Block(this, states.toMap())
    }

    fun withBlockStates(states: Map<String, String>): Block {
        return Block(this, states.toMap())
    }

    override fun getNbt(): CompoundBinaryTag? = null
}

@Serializable
data class RegistryBlockSounds(
    val breakSound: String,
    val hitSound: String,
    val placeSound: String,
    val fallSound: String,
    val walkSound: String,
)

@Serializable
data class RegistryBlockState(
    val name: String,
    val type: String,
    val values: List<String>? = null,
)