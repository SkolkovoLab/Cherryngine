package ru.cherryngine.platform.minecraft.java.world

/**
 * Трекер изменений в mutable слоях за текущий тик.
 * getDirty() возвращает snapshot (non-destructive), clear() вызывается в конце тика.
 */
class MutableLayerChangeTracker {
    private val dirtyChunks: MutableMap<String, MutableSet<ChunkPos>> = mutableMapOf()

    fun markDirty(layerId: String, chunkPos: ChunkPos) {
        dirtyChunks.getOrPut(layerId) { mutableSetOf() }.add(chunkPos)
    }

    fun getDirty(): Map<String, Set<ChunkPos>> = dirtyChunks.toMap()

    fun clear() {
        dirtyChunks.clear()
    }
}
