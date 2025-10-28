package ru.cherryngine.lib.minecraft.world.block

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.Blocks
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry
import ru.cherryngine.lib.minecraft.registry.registries.Item
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry
import ru.cherryngine.lib.minecraft.registry.registries.RegistryBlock
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class Block(
    val registryBlock: RegistryBlock,
    val blockStates: Map<String, String> = mutableMapOf(),
) {
    val identifier = registryBlock.identifier

    val tags = registryBlock.tags

    override fun equals(other: Any?): Boolean {
        if (other !is Block) return false
        return other.toString() == this.toString()
    }

    fun toItem(): Item {
        return ItemRegistry[identifier]
    }

    fun getProtocolId(): Int {
        if (blockStates.isEmpty()) return registryBlock.defaultBlockStateId
        if (registryBlock.states.isEmpty()) return registryBlock.defaultBlockStateId

        val id = registryBlock.possibleStates[this.asString()]
        return id ?: registryBlock.defaultBlockStateId
    }

    fun asString(): String {
        if (registryBlock.states.isEmpty()) return identifier

        val baseBlockStatesString = registryBlock.possibleStatesReversed[registryBlock.defaultBlockStateId]!!
        val (_, baseStates) = parseBlockStateString(baseBlockStatesString)

        val states = mutableMapOf<String, String>()
        baseStates.forEach { states[it.key] = it.value }
        blockStates.forEach { states[it.key] = it.value }

        val stringBuilder = kotlin.text.StringBuilder("$identifier[")
        states.entries.joinToString(separator = ",") { "${it.key}=${it.value}" }.also { stringBuilder.append(it) }

        return stringBuilder.append("]").toString()
    }

    override fun toString(): String {
        return asString()
    }

    fun withBlockStates(vararg states: Pair<String, String>): Block {
        return withBlockStates(states.toMap())
    }

    fun withBlockStates(states: Map<String, String>): Block {
        val newStates = blockStates.toMutableMap()
        newStates.putAll(states)
        return Block(registryBlock, newStates)
    }

    fun isAir(): Boolean {
        return this.registryBlock == Blocks.AIR
    }

    companion object {
        val AIR = Block(BlockRegistry.AIR)
        val STONE = Block(BlockRegistry["minecraft:stone"])

        val STREAM_CODEC = object : StreamCodec<Block> {
            override fun write(buffer: ByteBuf, value: Block) {
                StreamCodec.VAR_INT.write(buffer, value.getProtocolId())
            }

            override fun read(buffer: ByteBuf): Block {
                return getBlockByStateId(StreamCodec.VAR_INT.read(buffer))
            }
        }

        fun parseBlockStateString(string: String): Pair<String, Map<String, String>> {
            val index = string.indexOf('[')
            if (index == -1) return string to emptyMap()

            val block = string.substring(0, index)
            val statesPart = string.substring(index + 1, string.length - 1)

            val states = statesPart.split(',').associate {
                val (key, value) = it.split("=", limit = 2)
                key to value
            }

            return block to states
        }

        fun getBlockByStateId(stateId: Int): Block {
            if (stateId == 0) return AIR
            if (stateId == 1) return STONE

            val blockState = BlockRegistry.blockStates.get(stateId)
            if (blockState != null) {
                return blockState
            }

            val registryBlock = BlockRegistry.getByProtocolIdOrNull(stateId)
            if (registryBlock != null) {
                val states = registryBlock.possibleStatesReversed[registryBlock.defaultBlockStateId]!!
                val parsed = parseBlockStateString(states).second.toMutableMap()
                return Block(registryBlock, parsed)
            }

            for (block in BlockRegistry.getProtocolEntries()) {
                val cachedState = block.possibleStatesReversed
                if (cachedState.isEmpty()) continue
                if (!cachedState.containsKey(stateId)) continue

                val states = parseBlockStateString(cachedState[stateId]!!).second.toMutableMap()
                return Block(block, states)
            }
            throw kotlin.IllegalArgumentException("No block state found with $stateId")
        }

        fun getBlockFromStateString(identifier: String): Block {
            val blockIdentifier = identifier.split("[")[0]
            val registryBlock = BlockRegistry[blockIdentifier]

            //if no block state ids, no need to look up the block state ids map
            if (registryBlock.states.isEmpty()) {
                return registryBlock.toBlock()
            }

            val id = registryBlock.possibleStates[identifier]
                ?: throw kotlin.IllegalArgumentException("No matching state sequence found on ${registryBlock.identifier}")
            return getBlockByStateId(id)
        }

        fun getBlockFromStateStringFast(identifier: String, block: RegistryBlock): Block {
            val id = block.possibleStates[identifier]
                ?: throw kotlin.IllegalArgumentException("No matching state sequence found on ${block.identifier}")
            return getBlockByStateId(id)
        }
    }
}