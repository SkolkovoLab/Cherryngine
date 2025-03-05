package ru.cherryngine.engine.scenes

import io.micronaut.context.ApplicationContext
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.event.Event
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.collections.HashMap
import kotlin.reflect.KClass

class Scene(
    private val applicationContext: ApplicationContext,
    private val sceneManager: SceneManager,
    val data: Data,
) {
    val id: UUID = UUID.randomUUID()

    private val gameObjects: MutableMap<UUID, GameObject> = HashMap()
    private val parentGraph: Graph<UUID, DefaultEdge> = DirectedAcyclicGraph(DefaultEdge::class.java)

    private val eventQueue: AbstractQueue<Event> = ConcurrentLinkedQueue()

    var tick = 0L

    var tickStartMils = 0L
    var tickEndMils = 0L
    var tickElapsedMils = 0L

    val thread = Thread {
        while (isAlive) {

            tickStartMils = System.currentTimeMillis()

            tick()

            tickEndMils = System.currentTimeMillis()
            tickElapsedMils = tickEndMils - tickStartMils

            Thread.sleep(1000L / data.tps)
        }
        fireEvent(Events.Thread.Stop)
    }

    private var isAlive = false

    fun start() {

        isAlive = true
        fireEvent(Events.Thread.Start)

        thread.start()
    }

    fun stop() {
        isAlive = false
    }

    fun tick() {
        eventQueue.forEach(::fireEvent)
        eventQueue.clear()

        fireEvent(Events.Tick.Start)

        fireEvent(Events.Tick.Physics)

        fireEvent(Events.Tick.End)

        tick++
    }

    fun createGameObject(): GameObject {
        return GameObject(applicationContext, this).apply {
            fireEvent(GameObject.Events.Registration.Pre(this))
            gameObjects[id] = this
            parentGraph.addVertex(id)
            fireEvent(GameObject.Events.Registration.Post(this))
        }
    }

    fun destroyGameObject(id: UUID) {
        gameObjects.remove(id)?.onDestroy()
        parentGraph.removeVertex(id)
    }

    fun getGameObject(id: UUID): GameObject {
        return gameObjects[id] ?: throw NoSuchElementException()
    }

    fun <T : Module> getModules(clazz: KClass<T>): List<T> {
        return gameObjects.values.flatMap { it.getModules(clazz) }
    }

    fun fireEvent(event: Event) {
        if (!isAlive) return
        if (thread != Thread.currentThread()) {
            eventQueue.add(event)
            return
        }
        val modules = gameObjects.flatMap { it.value.modules }.groupBy { it::class }
        sceneManager.sortedModuleTypes.forEach { moduleType ->
            modules[moduleType]?.toList()?.forEach { module ->
                module.onEvent(event)
            }
        }
    }

    fun getParentId(childId: UUID): UUID? {
        return parentGraph.incomingEdgesOf(childId).firstOrNull()?.let(parentGraph::getEdgeSource)
    }

    fun getChildrenIds(parentId: UUID): List<UUID> {
        return parentGraph.outgoingEdgesOf(parentId).map(parentGraph::getEdgeTarget)
    }

    fun addChild(parentId: UUID, childId: UUID) {
        removeParent(childId)
        parentGraph.addEdge(parentId, childId)
    }

    fun removeParent(childId: UUID) {
        parentGraph.incomingEdgesOf(childId).toList().forEach(parentGraph::removeEdge)
    }

    fun removeChild(parentId: UUID, childId: UUID): Boolean {
        return parentGraph.removeEdge(parentId, childId) != null
    }

    fun removeAllChildren(parentId: UUID) {
        parentGraph.outgoingEdgesOf(parentId).toList().forEach(parentGraph::removeEdge)
    }

    data class Data(
        val tps: Int,
    )

    interface Events {

        interface Thread {

            object Start : Event

            object Stop : Event

        }

        interface Tick {

            object Start : Event

            object End : Event

            object Physics : Event

        }

    }
}