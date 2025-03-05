package ru.cherryngine.engine.scenes.modules.client

import io.micronaut.context.annotation.Parameter
import net.minestom.server.network.packet.client.play.ClientInputPacket
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.event.impl.ClientPacketEvent
import ru.cherryngine.engine.scenes.modules.client.api.Camera
import ru.cherryngine.engine.scenes.modules.client.api.Controller
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import kotlin.math.cos
import kotlin.math.sin

@ModulePrototype
class ThirdPersonController (
    @Parameter override val gameObject: GameObject,
    @Parameter val clientModule: ClientModule,
    @Parameter val camera: Camera
) : Module, Controller {

    var input: ClientInputPacket? = null

    override fun onEvent(event: Event) {
        when (event) {
            is ClientPacketEvent -> {
                if (clientModule.connection == event.clientConnection) {
                    onPacket(event)
                }
            }
            is Scene.Events.Tick.Physics -> {
                if (input != null) gameObject.transform.translation += getMovementDirection(input!!, camera.gameObject.transform.rotation.asView())
            }
        }
    }

    val onPacket: (ClientPacketEvent) -> Unit = {
        when (it.packet) {
            is ClientInputPacket -> {
                onInput(it.packet)
            }
        }
    }

    fun onInput(input: ClientInputPacket) {
        this.input = input
    }

    fun getMovementDirection(input: ClientInputPacket, view: View): Vec3D {
        val yawRad = Math.toRadians(view.yaw.toDouble())
        val pitchRad = Math.toRadians(view.pitch.toDouble())

        // Вычисляем направление на основе углов yaw и pitch
        val forward = Vec3D(
            x = cos(yawRad + Math.PI / 2) * cos(pitchRad),
            y = sin(-pitchRad),
            z = sin(yawRad + Math.PI / 2) * cos(pitchRad)
        )

        val right = Vec3D(
            x = sin(-yawRad + Math.PI / 2),
            y = 0.0,
            z = cos(-yawRad + Math.PI / 2)
        )

        var movementDirection = Vec3D(0.0, 0.0, 0.0)

        // Проверяем направления движения
        if (input.forward()) {
            movementDirection += forward
        }
        if (input.backward()) {
            movementDirection += forward * -1.0
        }
        if (input.left()) {
            movementDirection += right
        }
        if (input.right()) {
            movementDirection += right * -1.0
        }

        return movementDirection
    }
}