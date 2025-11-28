package ru.cherryngine.lib.minecraft.registry.registries

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.RegistryBlock
import ru.cherryngine.lib.minecraft.world.block.Block

object BlockRegistry : DataDrivenRegistry<RegistryBlock>(
    "minecraft:block",
    "registry/block_registry.json.gz",
    RegistryBlock.serializer()
) {
    val AIR get() = this["minecraft:air"]
    private var blockStatesNullable: MutableMap<Int, Block>? = null
    val blockStates: Map<Int, Block> get() = blockStatesNullable!!

    override fun addEntry(entry: RegistryBlock) {
        super.addEntry(entry)

        if (entry.states.isEmpty()) return

        entry.possibleStates.forEach { (stateString, stateId) ->
            val stateMap = Block.parseBlockStateString(stateString).second
            if (blockStatesNullable == null) blockStatesNullable = Int2ObjectOpenHashMap()
            blockStatesNullable!![stateId] = Block(entry, stateMap)
        }
    }

    fun getByStateIdOrNull(id: Int): RegistryBlock? {
        return blockStates.getOrDefault(id, null)?.registryBlock
    }
}
