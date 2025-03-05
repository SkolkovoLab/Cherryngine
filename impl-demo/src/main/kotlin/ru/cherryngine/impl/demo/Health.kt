package ru.cherryngine.impl.demo

import io.micronaut.context.annotation.Parameter
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View

@ModulePrototype
class Health(
    @Parameter override val gameObject: GameObject,
    @Parameter val maxHealth: Double,
    @Parameter val clientModule: ClientModule
) : Module{

    var health = maxHealth

    fun damage(amount: Double) {
        health  = (health - amount).coerceAtLeast(0.0)
        if (health <= 0.0) kill()
    }

    fun kill() {
        clientModule.let {
            gameObject.transform.translation = Vec3D(169.5, 73.5, 137.5)
            gameObject.transform.rotation = View.ZERO.getRotation()
            health = maxHealth
        }
    }

}