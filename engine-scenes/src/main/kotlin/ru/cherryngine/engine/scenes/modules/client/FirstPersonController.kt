package ru.cherryngine.engine.scenes.modules.client

import io.micronaut.context.annotation.Parameter
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket
import ru.cherryngine.engine.core.asQRot
import ru.cherryngine.engine.core.asVec3D
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.event.impl.ClientPacketEvent
import ru.cherryngine.engine.scenes.modules.client.api.Camera
import ru.cherryngine.engine.scenes.modules.client.api.Controller
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.math.rotation.QRot

@ModulePrototype
class FirstPersonController(
    @Parameter override val gameObject: GameObject,
    @Parameter val clientModule: ClientModule
) : Module, Controller, Camera {

    val onPacket: (ClientPacketEvent) -> Unit = {
        when (it.packet) {
            is ClientPlayerPositionPacket -> setPos(it.packet.position.asVec3D())
            is ClientPlayerPositionAndRotationPacket -> {
                setPos(it.packet.position.asVec3D())
                setRot(it.packet.position.asQRot())
            }
            is ClientPlayerRotationPacket -> {
                setRot(View(it.packet.yaw, it.packet.pitch).getRotation())
            }
        }
    }

    fun setPos(vec: Vec3D) {
        gameObject.transform.translation = vec
    }

    fun setRot(rot: QRot) {
        gameObject.transform.rotation = rot
    }

    override fun onEvent(event: Event) {
        when (event) {
            is ClientPacketEvent -> {
                if (clientModule.connection == event.clientConnection) {
                    onPacket(event)
                }
            }
        }
    }
}