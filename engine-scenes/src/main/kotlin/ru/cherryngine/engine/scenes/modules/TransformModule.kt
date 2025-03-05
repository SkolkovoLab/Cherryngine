package ru.cherryngine.engine.scenes.modules

import io.micronaut.context.annotation.Parameter
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot

@ModulePrototype
class TransformModule(
    @Parameter override val gameObject: GameObject,
) : Module {

    var translation = Vec3D.ZERO
    var rotation = QRot.IDENTITY
    var scale = Vec3D.ONE

    var local: Transform
        get() = Transform(translation, rotation, scale)
        set(value) {
            translation = value.translation
            rotation = value.rotation
            scale = value.scale
        }

    val global: Transform
        get() {
            val parent = gameObject.parent ?: return local
            return parent.transform.global * local
        }
}