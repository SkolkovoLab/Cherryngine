package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import net.minestom.server.registry.Registries
import net.minestom.server.world.DimensionType
import ru.cherryngine.engine.core.world.BlockHolder
import ru.cherryngine.engine.core.world.PolarChunkSupplier
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.SceneManager
import ru.cherryngine.engine.scenes.modules.BlockHolderModule
import ru.cherryngine.engine.scenes.modules.ViewSynchronizer
import ru.cherryngine.engine.scenes.modules.physics.SimplePhysics

@Singleton
class Demo(
    sceneManager: SceneManager,
    registries: Registries,
) {
    val masterScene: Scene = sceneManager.createScene(data = Scene.Data(20))

    init {
        val blockHolder = BlockHolder(
            registries.dimensionType().get(DimensionType.OVERWORLD)!!,
            PolarChunkSupplier(javaClass.getResource("/world.polar")!!.readBytes(), registries)
        )

        masterScene.createGameObject().getOrCreateModule(BlockHolderModule::class, blockHolder)

        masterScene.createGameObject().apply {
            getOrCreateModule(ViewSynchronizer::class)
            getOrCreateModule(SimplePhysics::class)
        }
    }
}