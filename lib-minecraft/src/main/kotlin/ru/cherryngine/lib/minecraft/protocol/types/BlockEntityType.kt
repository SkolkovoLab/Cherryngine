package ru.cherryngine.lib.minecraft.protocol.types

import net.kyori.adventure.key.Key

enum class BlockEntityType {
    FURNACE,
    CHEST,
    TRAPPED_CHEST,
    ENDER_CHEST,
    JUKEBOX,
    DISPENSER,
    DROPPER,
    SIGN,
    HANGING_SIGN,
    MOB_SPAWNER,
    CREAKING_HEART,
    PISTON,
    BREWING_STAND,
    ENCHANTING_TABLE,
    END_PORTAL,
    BEACON,
    SKULL,
    DAYLIGHT_DETECTOR,
    HOPPER,
    COMPARATOR,
    BANNER,
    STRUCTURE_BLOCK,
    END_GATEWAY,
    COMMAND_BLOCK,
    SHULKER_BOX,
    BED,
    CONDUIT,
    BARREL,
    SMOKER,
    BLAST_FURNACE,
    LECTERN,
    BELL,
    JIGSAW,
    CAMPFIRE,
    BEEHIVE,
    SCULK_SENSOR,
    CALIBRATED_SCULK_SENSOR,
    SCULK_CATALYST,
    SCULK_SHRIEKER,
    CHISELED_BOOKSHELF,
    BRUSHABLE_BLOCK,
    DECORATED_POT,
    CRAFTER,
    TRIAL_SPAWNER,
    VAULT,
    TEST_BLOCK,
    TEST_INSTANCE_BLOCK;

    val key = Key.key(name.lowercase())
    val id get() = ordinal

    companion object {
        private val BY_KEY = entries.associateBy { it.key }
        fun fromKey(key: Key): BlockEntityType {
            return BY_KEY[key] ?: throw IllegalArgumentException("BlockEntityType $key not found")
        }
        fun fromId(id: Int): BlockEntityType {
            return entries[id]
        }
    }
}