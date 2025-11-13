package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.ecs.testimpl.components.ApartComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D

class ApartSystem : IteratingSystem(
    family { all(PlayerComponent, PositionComponent, ApartComponent) }
) {
    val enterCuboid = Cuboid(Vec3D(-1.0, -1.0, 7.0), Vec3D(1.0, 3.0, 8.0))
    val exitCuboid = Cuboid(Vec3D(-1.0, -1.0, 8.0), Vec3D(1.0, 3.0, 9.0))
    val enterPosition = Vec3D(0.0, 0.0, 9.5)
    val exitPosition = Vec3D(0.0, 0.0, 6.5)

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val positionComponent = entity[PositionComponent]

        if (enterCuboid.isInside(positionComponent.position)) {
            val apartName = entity.getOrNull(ApartComponent)?.apartName
            if (apartName != null) {
                positionComponent.position = enterPosition
                playerComponent.viewContextIDs = setOf("street", apartName)
                entity.getOrNull(ViewableComponent)?.viewContextIDs = setOf(apartName)
            }
        }

        if (exitCuboid.isInside(positionComponent.position)) {
            positionComponent.position = exitPosition

            playerComponent.viewContextIDs = setOf("street")
            entity.getOrNull(ViewableComponent)?.viewContextIDs = setOf("street")
        }
    }
}