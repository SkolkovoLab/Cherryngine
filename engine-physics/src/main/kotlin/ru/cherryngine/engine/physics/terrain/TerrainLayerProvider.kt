package ru.cherryngine.engine.physics.terrain

interface TerrainLayerProvider {
    fun collectLayers(): List<LayerWithContext>
}
