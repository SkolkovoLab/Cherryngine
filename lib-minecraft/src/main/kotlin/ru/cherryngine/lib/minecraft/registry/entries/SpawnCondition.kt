package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class SpawnCondition(
    val condition: JsonObject? = null, // TODO
    val priority: Int,
)