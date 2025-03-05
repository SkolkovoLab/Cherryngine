package ru.cherryngine.impl.demo

import io.micronaut.context.annotation.Parameter
import net.minestom.server.network.packet.client.play.ClientAnimationPacket
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.event.impl.ClientPacketEvent
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.engine.scenes.modules.physics.CuboidCollider
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D

@ModulePrototype
class Shooter(
    @Parameter override val gameObject: GameObject,
    @Parameter val clientModule: ClientModule
) : Module {

    override fun onEvent(event: Event) {
        when (event) {
            is ClientPacketEvent -> {
                when (event.packet) {
                    is ClientAnimationPacket -> {
                        shoot(event.clientConnection)
                    }
                }
            }
        }
    }

    fun shoot(clientConnection: ClientConnection) {
        if (clientModule.connection != clientConnection) return
        scene.createGameObject().let {
            it.transform.translation = gameObject.transform.global.translation + gameObject.transform.global.scale * Vec3D(0.0, 0.85, 0.0)
            it.transform.rotation = gameObject.transform.global.rotation
            it.transform.scale = Vec3D.ONE * .5
            it.getOrCreateModule(Projectile::class, this)
            it.getOrCreateModule(CuboidCollider::class,
                Cuboid.fromTwoPoints(it.transform.global.scale * Vec3D(-.5, -.5, -.5), it.transform.global.scale * Vec3D(.5, .5, .5))
            )
        }
    }
}