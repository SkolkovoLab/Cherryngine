package ru.cherryngine.lib.minecraft.registry.registries

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import ru.cherryngine.lib.minecraft.r2.Registries
import ru.cherryngine.lib.minecraft.registry.entries.RegistryBlock
import ru.cherryngine.lib.minecraft.world.block.Block

object BlockRegistry {
    val AIR get() = Registries.block["minecraft:air"]
    private var blockStatesNullable: MutableMap<Int, Block>? = null
    val blockStates: Map<Int, Block> get() = blockStatesNullable!!

    init {
        Registries.block.values.forEach { entry ->
            entry.possibleStates.forEach { (stateString, stateId) ->
                val stateMap = Block.parseBlockStateString(stateString).second
                if (blockStatesNullable == null) blockStatesNullable = Int2ObjectOpenHashMap()
                blockStatesNullable!![stateId] = Block(entry, stateMap)
            }
        }
    }

    fun getByStateIdOrNull(id: Int): RegistryBlock? {
        return blockStates.getOrDefault(id, null)?.registryBlock
    }

    fun getByStateId(id: Int): RegistryBlock {
        return getByStateIdOrNull(id)!!
    }
}
