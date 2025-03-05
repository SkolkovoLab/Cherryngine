package ru.cherryngine.engine.scenes.modules

import io.micronaut.context.annotation.Parameter
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.view.Viewable
import ru.cherryngine.engine.scenes.view.Viewer

@ModulePrototype
class ViewSynchronizer(
    @Parameter override val gameObject: GameObject
) : Synchronizer {

    private val graph: Graph<Module, DefaultEdge> = DirectedAcyclicGraph(DefaultEdge::class.java)

    fun synchronize(viewer: Viewer, viewable: Viewable) {
        if (!graph.containsVertex(viewable)) {
            graph.addVertex(viewable)
        }
        if (!graph.containsVertex(viewer)) {
            graph.addVertex(viewer)
        }

        if (canBeSeen(viewer, viewable)) {
            if (!graph.containsEdge(viewable, viewer) && viewable.showFor(viewer)) {
                graph.addEdge(viewable, viewer)
            }
        } else {
            if (graph.containsEdge(viewable, viewer) && viewable.hideFor(viewer)) {
                graph.removeEdge(viewable, viewer)
            }
        }
    }

    fun canBeSeen(viewer: Viewer, viewable: Viewable): Boolean {
        return viewer.gameObject != viewable.gameObject
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Scene.Events.Tick.End -> {
                scene.getModules(Viewable::class).forEach { viewable ->
                    scene.getModules(Viewer::class).forEach { viewer ->
                        synchronize(viewer, viewable)
                    }
                }
            }
            is Module.Events.Destroy.Pre -> {
                when (val module = event.module) {
                    is Viewable -> {
                        if (graph.containsVertex(module)) {
                            graph.outgoingEdgesOf(module).forEach {
                                val viewer = graph.getEdgeTarget(it) as Viewer
                                module.hideFor(viewer)
                            }
                        }
                    }
                    is Viewer -> {
                        if (graph.containsVertex(module)) {
                            graph.incomingEdgesOf(module).forEach {
                                val viewable = graph.getEdgeSource(it) as Viewable
                                viewable.hideFor(module)
                            }
                        }
                    }
                }
            }
        }
    }

}