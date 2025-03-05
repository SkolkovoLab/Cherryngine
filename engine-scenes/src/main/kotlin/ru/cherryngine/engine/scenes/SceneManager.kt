@file:Suppress("unused")

package ru.cherryngine.engine.scenes

import io.micronaut.context.ApplicationContext
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.inject.Singleton
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.jgrapht.traverse.TopologicalOrderIterator
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Singleton
class SceneManager(
    private val applicationContext: ApplicationContext,
) {
    val scenes: MutableMap<UUID, Scene> = HashMap()

    lateinit var sortedModuleTypes: List<KClass<out Module>>
        private set

    @PostConstruct
    fun init() {
        //region Module sorting
        val moduleTypes = applicationContext.getBeanDefinitions(Module::class.java).map { it.beanType.kotlin }

        val graph: Graph<KClass<out Module>, DefaultEdge> = DirectedAcyclicGraph(DefaultEdge::class.java)
        moduleTypes.forEach(graph::addVertex)
        moduleTypes.forEach { moduleType ->
            val annotation = moduleType.findAnnotation<ModulePrototype>() ?: return@forEach
            annotation.tickAfter.forEach { clazz ->
                val after = moduleTypes.first { it::class == clazz }
                graph.addEdge(moduleType, after)
            }
            annotation.tickBefore.forEach { clazz ->
                val before = moduleTypes.first { it::class == clazz }
                graph.addEdge(before, moduleType)
            }
        }

        val priorityMap = moduleTypes.associateWith { moduleType ->
            val annotation = moduleType.findAnnotation<ModulePrototype>()
            val priority = annotation?.tickPriority ?: ModulePrototype.Priority.NORMAL
            priority.ordinal
        }

        val priorityComparator = Comparator.comparingInt<KClass<out Module>> { priorityMap[it]!! }

        sortedModuleTypes = TopologicalOrderIterator(graph, priorityComparator).asSequence().toList()
        //endregion
    }

    @PreDestroy
    fun destroy() {
        scenes.values.forEach(Scene::stop)
        scenes.clear()
    }

    fun removeScene(id: UUID) {
        scenes.remove(id)?.stop()
    }

    fun createScene(data: Scene.Data): Scene {
        return Scene(applicationContext, this, data).also {
            scenes[it.id] = it
            it.start()
        }
    }

    fun fireGlobalEvent(event: Event) {
        scenes.values.forEach { it.fireEvent(event) }
    }

}