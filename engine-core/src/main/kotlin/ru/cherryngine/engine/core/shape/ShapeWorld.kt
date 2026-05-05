package ru.cherryngine.engine.core.shape

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.lib.math.Transform
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@InstanceSingleton
class ShapeWorld {
    private data class Entry(
        val shape: Shape,
        val registration: ShapeRegistration,
    )

    private val entries = ConcurrentHashMap<UUID, Entry>()

    // Регистрация одного шейпа
    fun register(shape: Shape): ShapeRegistration {
        val registration = ShapeRegistration(this)
        entries[registration.id] = Entry(shape, registration)
        return registration
    }

    // Регистрация группы шейпов одного владельца — удаляются все сразу
    fun registerGroup(shapes: List<Shape>): ShapeGroupRegistration {
        val registrations = shapes.map { register(it) }
        return ShapeGroupRegistration(registrations)
    }

    fun unregister(id: UUID) {
        entries.remove(id)
    }

    // Запрос всех шейпов с актуальными трансформами
    fun query(filter: ShapeFilter = ShapeFilter.ALL): List<ResolvedShape> {
        return entries.values
            .asSequence()
            .filter { filter.test(it.shape) }
            .mapNotNull { entry ->
                val transform = runCatching { entry.shape.getTransform() }.getOrNull()
                    ?: return@mapNotNull null  // источник недоступен — пропускаем
                ResolvedShape(entry.shape, transform)
            }
            .toList()
    }
}

// Результат запроса — шейп с актуальным трансформом
data class ResolvedShape(
    val shape: Shape,
    val transform: Transform,
)

// Фильтр для query
fun interface ShapeFilter {
    fun test(shape: Shape): Boolean

    companion object {
        val ALL = ShapeFilter { true }
    }
}

// Групповая регистрация — закрывает все шейпы группы одним вызовом
class ShapeGroupRegistration(
    private val registrations: List<ShapeRegistration>,
) : AutoCloseable {
    override fun close() = registrations.forEach { it.close() }
}
