package ru.cherryngine.lib.minecraft.world.block

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Blocks
import ru.cherryngine.lib.minecraft.registry.types.Item
import ru.cherryngine.lib.minecraft.registry.types.RegistryBlock

data class Block(
    val registryBlock: RegistryBlock,
    val blockStates: Map<String, String> = mapOf(),
) {
    val identifier = registryBlock.key.toString()

    override fun equals(other: Any?): Boolean {
        if (other !is Block) return false
        return other.asString() == this.asString()
    }

    override fun hashCode(): Int {
        return asString().hashCode()
    }

    fun toItem(): Item {
        return Registries.item[registryBlock.key].value
    }

    fun getStateId(): Int {
        if (blockStates.isEmpty()) return registryBlock.defaultStateId
        if (registryBlock.states.isEmpty()) return registryBlock.defaultStateId

        val id = registryBlock.possibleStates[this.asString()]
        return id ?: registryBlock.defaultStateId
    }

    fun asString(): String {
        if (registryBlock.states.isEmpty()) return identifier

        val baseBlockStatesString = registryBlock.possibleStatesReversed[registryBlock.defaultStateId]!!
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
        return this.registryBlock.air
    }

    companion object {
        val AIR by lazy { Block(Registries.block[Blocks.AIR].value) }
        val STRUCTURE_VOID by lazy { Block(Registries.block[Blocks.STRUCTURE_VOID].value) }

        val STREAM_CODEC = object : StreamCodec<Block> {
            override fun write(buffer: ByteBuf, value: Block) {
                StreamCodec.VAR_INT.write(buffer, value.getStateId())
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

            val blockState = BlockStates.blockStates.get(stateId)
            if (blockState != null) {
                return blockState
            }

            val registryBlock = BlockStates.getByStateIdOrNull(stateId)
            if (registryBlock != null) {
                val states = registryBlock.possibleStatesReversed[registryBlock.defaultStateId]!!
                val parsed = parseBlockStateString(states).second.toMutableMap()
                return Block(registryBlock, parsed)
            }

            for (block in Registries.block.values) {
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
            val registryBlock = Registries.block[blockIdentifier].value

            //if no block state ids, no need to look up the block state ids map
            if (registryBlock.states.isEmpty()) {
                return registryBlock.toBlock()
            }

            val id = registryBlock.possibleStates[identifier]
                ?: throw kotlin.IllegalArgumentException("No matching state sequence found on ${registryBlock.key}")
            return getBlockByStateId(id)
        }

        fun getBlockFromStateStringFast(identifier: String, block: RegistryBlock): Block {
            val id = block.possibleStates[identifier]
                ?: throw kotlin.IllegalArgumentException("No matching state sequence found on ${block.key}")
            return getBlockByStateId(id)
        }
    }
}