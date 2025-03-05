package ru.cherryngine.engine.scenes.modules.client

import io.micronaut.context.annotation.Parameter
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.RelativeFlags
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket
import net.minestom.server.network.packet.server.play.PlayerPositionAndLookPacket
import ru.cherryngine.engine.core.asQRot
import ru.cherryngine.engine.core.minestomPos
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.event.impl.ClientPacketEvent
import ru.cherryngine.engine.scenes.modules.client.api.Camera
import ru.cherryngine.engine.scenes.modules.client.api.Controller
import ru.cherryngine.lib.math.View
import ru.cherryngine.lib.math.rotation.QRot

@ModulePrototype
class FocusCamera(
    @Parameter override val gameObject: GameObject,
    @Parameter val clientModule: ClientModule,
    @Parameter val target: GameObject
) : Module, Camera, Controller {

    //val cameraRotation: QRot = QRot.fromAxisAngle(Vec3D.ZERO, 0.0)

    val onPacket: (ClientPacketEvent) -> Unit = {
        when (it.packet) {
            // is ClientPlayerPositionPacket -> setPos(it.packet.position.asVec3D())
            is ClientPlayerPositionAndRotationPacket -> {
                // setPos(it.packet.position.asVec3D())
                setRot(it.packet.position.asQRot())
            }
            is ClientPlayerRotationPacket -> {
                setRot(View(it.packet.yaw, it.packet.pitch).getRotation())
            }
        }
    }


    fun setRot(rot: QRot) {
        gameObject.transform.rotation = rot
    }

    override fun onEvent(event: Event) {
        when (event) {

            is Scene.Events.Tick.End -> {
                gameObject.transform.translation = target.transform.translation - gameObject.transform.rotation.asView().direction() * 5.0
                val view = gameObject.transform.global.rotation.asView()
                clientModule.connection.sendPacket(
                    PlayerPositionAndLookPacket(
                        -1,
                        gameObject.transform.translation.minestomPos(),
                        Vec.ZERO,
                        view.yaw,
                        view.pitch,
                        RelativeFlags.NONE
                    )
                )
            }

            is ClientPacketEvent -> {
                if (clientModule.connection == event.clientConnection) {
                    onPacket(event)
                }
            }

        }
    }
}