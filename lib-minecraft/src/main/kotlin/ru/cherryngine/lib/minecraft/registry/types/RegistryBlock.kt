package ru.cherryngine.lib.minecraft.registry.types

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.utils.extentions.reversed
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer
import ru.cherryngine.lib.minecraft.utils.registry.StaticProtocolObject
import ru.cherryngine.lib.minecraft.world.block.Block

@Serializable
data class RegistryBlock(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
    val translationKey: String,
    val explosionResistance: Float,
    val friction: Float,
    val speedFactor: Float = 1f,
    val jumpFactor: Float = 1f,
    val defaultStateId: Int,
    val gravity: Boolean = false,
    val correspondingItem: String? = null,
    val maxHorizontalOffset: Float = 0f,
    val maxVerticalOffset: Float = 0f,
    val canRespawnIn: Boolean,
    val hardness: Float,
    val lightEmission: Int = 0,
    val pushReaction: String, // TODO enum
    val mapColorId: Int,
    val occludes: Boolean,
    val requiresTool: Boolean,
    val blocksMotion: Boolean,
    val flammable: Boolean,
    val liquid: Boolean = false,
    val air: Boolean = false,
    val replaceable: Boolean = false,
    val solid: Boolean,
    val solidBlocking: Boolean,
    val soundType: String,
    @Serializable(ShapeSerializer::class)
    val shape: Shape,
    @Serializable(ShapeSerializer::class)
    val collisionShape: Shape,
    @Serializable(ShapeSerializer::class)
    val interactionShape: Shape,
    @Serializable(ShapeSerializer::class)
    val occlusionShape: Shape,
    @Serializable(ShapeSerializer::class)
    val visualShape: Shape,
    val redstoneConductor: Boolean,
    val signalSource: Boolean = false,
    val properties: Map<String, List<String>> = emptyMap(),
    val states: Map<String, RegistryBlockState>,
    val blockEntity: RegistryBlockEntity? = null,
) : StaticProtocolObject {
    @Contextual
    val possibleStates = states.asSequence().associate {
        val stateKey = if (it.key == "[]") key.toString() else key.toString() + it.key
        stateKey to it.value.stateId
    }

    @Contextual
    val possibleStatesReversed = Int2ObjectOpenHashMap(possibleStates.reversed())

    fun toItem(): Item {
        return Registries.item[key].value
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
        val stateId: Int,
        val canRespawnIn: Boolean? = null,
        val blocksMotion: Boolean? = null,
        val lightEmission: Int? = null,
        val mapColorId: Int? = null,
        val flammable: Boolean? = null,
        val solid: Boolean? = null,
        val solidBlocking: Boolean? = null,
        val soundType: String? = null,
        @Serializable(ShapeSerializer::class)
        val shape: Shape? = null,
        @Serializable(ShapeSerializer::class)
        val collisionShape: Shape? = null,
        @Serializable(ShapeSerializer::class)
        val interactionShape: Shape? = null,
        @Serializable(ShapeSerializer::class)
        val occlusionShape: Shape? = null,
        @Serializable(ShapeSerializer::class)
        val visualShape: Shape? = null,
        val redstoneConductor: Boolean? = null,
    )

    @Serializable
    data class RegistryBlockEntity(
        val namespace: String,
        val id: Int,
    )
}

data class Shape(
    val cuboids: List<Cuboid>,
)

object ShapeSerializer : KSerializer<Shape> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CuboidList", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Shape {
        val raw = decoder.decodeString().trim()
        if (raw.isBlank() || raw == "[]") return Shape(emptyList())

        val content = raw.removePrefix("[").removeSuffix("]")

        val parts = splitTopLevel(content)
            .filter { it.isNotEmpty() }

        val cuboids = parts.map { part ->
            // Ожидается "AABB[min] -> [max]"
            val cleaned = part.removePrefix("AABB").trim()

            val (minStr, maxStr) = cleaned.split("->")
                .map { it.trim() }

            Cuboid(
                min = parseVec(minStr),
                max = parseVec(maxStr)
            )
        }

        return Shape(cuboids)
    }

    private fun parseVec(s: String): Vec3D {
        val nums = s.removePrefix("[")
            .removeSuffix("]")
            .split(",")
            .map { it.trim().toDouble() }

        return Vec3D(nums[0], nums[1], nums[2])
    }

    private fun splitTopLevel(str: String): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var depth = 0

        for (c in str) {
            when (c) {
                '[' -> {
                    depth++
                    sb.append(c)
                }

                ']' -> {
                    depth--
                    sb.append(c)
                }

                ',' -> {
                    if (depth == 0) {
                        result += sb.toString().trim()
                        sb.clear()
                    } else sb.append(c)
                }

                else -> sb.append(c)
            }
        }

        if (sb.isNotEmpty()) {
            result += sb.toString().trim()
        }

        return result
    }

    override fun serialize(encoder: Encoder, value: Shape) {
        val result = value.cuboids.joinToString(prefix = "[", postfix = "]") { c ->
            "AABB[${c.min.x}, ${c.min.y}, ${c.min.z}] -> [${c.max.x}, ${c.max.y}, ${c.max.z}]"
        }
        encoder.encodeString(result)
    }
}

