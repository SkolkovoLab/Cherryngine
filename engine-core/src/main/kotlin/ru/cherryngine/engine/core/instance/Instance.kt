package ru.cherryngine.engine.core.instance

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import ru.cherryngine.engine.core.utils.StableTicker
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

class Instance(
    val tickDuration: Duration,
    val platformIds: Set<String>,
    private val appContext: ApplicationContext,
) : AutoCloseable {

    private val cache = ConcurrentHashMap<Class<*>, Any>()

    init {
        cache[Instance::class.java] = this
    }

    fun <T : Any> register(type: Class<T>, instance: T) {
        cache[type] = instance
    }

    inline fun <reified T : Any> register(instance: T) = register(T::class.java, instance)

    inline fun <reified T : Any> get(): T = get(T::class.java)

    fun <T : Any> get(type: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        (cache[type] as? T)?.let { return it }
        val definition = appContext.getBeanDefinition(type)
        return InstanceSingletonScope.withInstance(this) {
            appContext.getBean(definition)
        }
    }

    inline fun <reified T : Any> getAll(): List<T> = getAll(T::class.java)

    fun <T : Any> getAll(type: Class<T>): List<T> {
        return appContext.getBeanDefinitions(type)
            .filter { definition ->
                val platform = definition
                    .stringValue(InstanceSingleton::class.java, InstanceSingleton::platform.name)
                    .orElse("")
                platform.isEmpty() || platform in platformIds
            }
            .map { definition ->
                InstanceSingletonScope.withInstance(this) {
                    appContext.getBean(definition)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> getOrCreate(type: Class<T>, factory: () -> T): T =
        cache.getOrPut(type) { factory() } as T

    fun initEager() {
        appContext.getBeanDefinitions(
            Qualifiers.byStereotype<Any>(InstanceSingleton::class.java)
        ).filter {
            it.booleanValue(InstanceSingleton::class.java, InstanceSingleton::eagerInit.name).orElse(false)
        }.forEach {
            @Suppress("UNCHECKED_CAST")
            get(it.beanType as Class<Any>)
        }
    }

    private lateinit var stagedTickables: List<Tickable>

    fun startTicking() {
        val allTickables = appContext.getBeanDefinitions(Tickable::class.java)
            .filter { definition ->
                if (!definition.hasStereotype(InstanceSingleton::class.java)) return@filter false
                val platform = definition
                    .stringValue(InstanceSingleton::class.java, InstanceSingleton::platform.name)
                    .orElse("")
                platform.isEmpty() || platform in platformIds
            }

        stagedTickables = TickStage.entries.flatMap { stage ->
            allTickables.filter { definition ->
                val beanStage = definition
                    .enumValue(InstanceSingleton::class.java, InstanceSingleton::stage.name, TickStage::class.java)
                    .orElse(TickStage.GAME)
                beanStage == stage
            }.map { definition ->
                InstanceSingletonScope.withInstance(this) {
                    appContext.getBean(definition)
                }
            }
        }

        ticker.start()
    }

    private val ticker = StableTicker(tickDuration) { _, _ ->
        stagedTickables.forEach { it.tick(tickDuration) }
    }

    fun stop() = ticker.stop()

    override fun close() {
        stop()
        cache.values.filterIsInstance<AutoCloseable>().forEach { runCatching { it.close() } }
        cache.clear()
    }
}
