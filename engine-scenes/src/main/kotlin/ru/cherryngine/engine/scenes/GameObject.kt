@file:Suppress("unused")

package ru.cherryngine.engine.scenes

import io.micronaut.context.ApplicationContext
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.modules.TransformModule
import java.util.*
import kotlin.reflect.KClass

class GameObject(
    private val applicationContext: ApplicationContext,
    val scene: Scene,
) {

    val id: UUID = UUID.randomUUID()

    val transform: TransformModule = TransformModule(this)

    var parent: GameObject?
        get() = scene.getParentId(id)?.let(scene::getGameObject)
        set(value) {
            value?.let { setParent(it.id) }
        }

    val children: List<GameObject>
        get() = scene.getChildrenIds(id).map(scene::getGameObject)

    private val _modules: MutableMap<KClass<out Module>, Module> = hashMapOf(
        TransformModule::class to transform
    )

    val modules get() = _modules.values.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T : Module> getOrCreateModule(clazz: KClass<T>, vararg args: Any): T {
        return _modules.computeIfAbsent(clazz) {
            try {
                createAndEnableModule(clazz, *args)
            } catch (e: Exception) {
                throw IllegalArgumentException("Failed to create module of type ${clazz.simpleName}", e)
            }
        } as T
    }

    private fun <T : Module> createAndEnableModule(clazz: KClass<T>, vararg args: Any): T {
        return applicationContext.createBean(clazz.java, this, *args).apply {
            scene.fireEvent(Module.Events.Enable.Pre(this))
            enable()
            scene.fireEvent(Module.Events.Enable.Post(this))
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Module> getModule(clazz: KClass<T>): T? {
        return _modules[clazz] as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Module> getModules(clazz: KClass<T>): List<T> {
        return _modules.values.filter {
            clazz.isInstance(it)
        }.map { it as T }.toList()
    }

    fun setParent(parent: UUID) {
        scene.addChild(parent, id)
    }

    fun addChild(child: UUID) {
        scene.addChild(id, child)
    }

    fun addChild(child: GameObject) {
        scene.addChild(id, child.id)
    }

    fun removeAllChildren() {
        scene.removeAllChildren(id)
    }

    fun removeChild(child: UUID) {
        scene.removeChild(id, child)
    }

    fun removeChild(child: GameObject) {
        scene.removeChild(id, child.id)
    }

    fun removeParent() {
        scene.removeParent(id)
    }

    fun destroy() {
        scene.destroyGameObject(id)
    }

    fun onDestroy() {
        scene.fireEvent(Events.Destroy.Pre(this))

        _modules.values.forEach {

            scene.fireEvent(Module.Events.Destroy.Pre(it))
            it.destroy()
            scene.fireEvent(Module.Events.Destroy.Pre(it))

        }

        _modules.clear()
        removeAllChildren()

        scene.fireEvent(Events.Destroy.Post(this))
    }

    interface Events {

        interface Destroy {

            data class Pre (
                val gameObject: GameObject
            ) : Event

            data class Post (
                val gameObject: GameObject
            ) : Event

        }

        interface Registration {

            data class Pre (
                val gameObject: GameObject
            ) : Event

            data class Post (
                val gameObject: GameObject
            ) : Event

        }

        interface Parent {

            interface Change {

                data class Pre (
                    val gameObject: GameObject,
                    val parent: GameObject
                ) : Event

                data class Post (
                    val gameObject: GameObject,
                    val parent: GameObject
                ) : Event

            }

        }

    }
}
