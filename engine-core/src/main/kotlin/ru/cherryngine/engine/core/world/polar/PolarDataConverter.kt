package ru.cherryngine.engine.core.world.polar

import net.kyori.adventure.nbt.CompoundBinaryTag
import java.util.Map

/**
 * Allows for upgrading world data from one game version to another.
 */
interface PolarDataConverter {
    /**
     * Returns the data version to use on worlds lower than [PolarWorld.VERSION_DATA_CONVERTER] which
     * do not store a data version. Defaults to the current Minestom data version.
     */
    fun defaultDataVersion(): Int {
        return PolarWorld.DATA_VERSION
    }

    /**
     * Returns the current data version of the world.
     */
    fun dataVersion(): Int {
        return PolarWorld.DATA_VERSION
    }

    /**
     *
     * Converts the block palette from one version to another. Implementations are expected to modify
     * the palette array in place.
     *
     * @param palette     An array of block namespaces, eg "minecraft:stone_stairs[facing=north]"
     * @param fromVersion The data version of the palette
     * @param toVersion   The data version to convert the palette to
     */
    fun convertBlockPalette(palette: Array<String>?, fromVersion: Int, toVersion: Int) {
    }

    fun convertBlockEntityData(
        id: String, data: CompoundBinaryTag,
        fromVersion: Int, toVersion: Int
    ): MutableMap.MutableEntry<String, CompoundBinaryTag> {
        return Map.entry<String, CompoundBinaryTag>(id, data)
    }

    companion object {
        val NOOP: PolarDataConverter = object : PolarDataConverter {
        }
    }
}
