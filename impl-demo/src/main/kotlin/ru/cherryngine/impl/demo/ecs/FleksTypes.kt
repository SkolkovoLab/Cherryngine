package ru.cherryngine.impl.demo.ecs

import com.github.quillraven.fleks.EntityComponentContext
import ru.cherryngine.impl.demo.ecs.testimpl.components.EventsComponent

typealias FleksWorld = com.github.quillraven.fleks.World
typealias FleksEntity = com.github.quillraven.fleks.Entity

context(entityComponentContext: EntityComponentContext)
fun FleksEntity.eventsComponent(): EventsComponent {
    with(entityComponentContext) {
        var eventsComponent: EventsComponent? = null
        configure {
            eventsComponent = it.getOrAdd(EventsComponent, ::EventsComponent)
        }
        return eventsComponent!!
    }
}