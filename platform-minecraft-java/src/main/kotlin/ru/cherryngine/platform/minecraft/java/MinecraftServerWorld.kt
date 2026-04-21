package ru.cherryngine.platform.minecraft.java

import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import ru.cherryngine.engine.core.instance.ServerWorld
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.world.LayerEntry
import ru.cherryngine.platform.minecraft.java.world.LayeredWorld

/**
 * Minecraft-реализация [ServerWorld]: реестр слоёв по context-id плюс кэш
 * скомпонованных `LayeredWorld` для каждого набора context-id.
 *
 * Per-instance объект — регистрируется в `Instance` через `InstanceFactory`.
 */
class MinecraftServerWorld : ServerWorld {
    private val layersByContext = HashMap<String, MutableList<LayerEntry>>()
    private val worldCache = HashMap<Set<String>, LayeredWorld>()

    var dimensionType: DimensionType? = null
        set(value) {
            field = value
            worldCache.clear()
        }

    fun registerLayer(contextID: String, entry: LayerEntry) {
        layersByContext.getOrPut(contextID) { mutableListOf() }.add(entry)
        worldCache.clear()
    }

    fun getLayersForContexts(contextIDs: Set<String>): List<LayerEntry> =
        contextIDs.flatMap { layersByContext[it] ?: emptyList() }

    fun getLayersByContext(): Map<String, List<LayerEntry>> = layersByContext

    fun getLayeredWorld(contextIDs: Set<String>): LayeredWorld? {
        val dt = dimensionType ?: return null
        val layers = getLayersForContexts(contextIDs)
        if (layers.isEmpty()) return null
        return worldCache.getOrPut(contextIDs) { LayeredWorld(dt, layers) }
    }

    fun getBlock(pos: Vec3I, contextIDs: Set<String>): Block =
        getLayeredWorld(contextIDs)?.getBlock(pos) ?: Block.AIR
}
