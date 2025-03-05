package ru.cherryngine.engine.scenes.modules

import io.micronaut.context.annotation.Parameter
import net.minestom.server.coordinate.ChunkRange
import ru.cherryngine.engine.core.minestomPos
import ru.cherryngine.engine.core.world.BlockHolder
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.engine.scenes.view.Viewable
import ru.cherryngine.engine.scenes.view.Viewer

@ModulePrototype
class BlockHolderModule(
    @Parameter override val gameObject: GameObject,
    @Parameter val blockHolder: BlockHolder
) : Module, Viewable {

    override fun showFor(viewer: Viewer): Boolean {
        return when (viewer) {
            is ClientModule -> {
                val pos = viewer.gameObject.transform.global.translation.minestomPos()
                var packetSent = false

                ChunkRange.chunksInRange(pos.chunkX(), pos.chunkZ(), viewer.viewDistance) { x, z ->
                    blockHolder.generatePacket(x, z)?.let { packet ->
                        viewer.connection.sendPacket(packet)
                        packetSent = true
                    }
                }
                packetSent
            }
            else -> viewer.show(this)
        }
    }

    override fun hideFor(viewer: Viewer): Boolean {
        return viewer.hide(this)
    }
}