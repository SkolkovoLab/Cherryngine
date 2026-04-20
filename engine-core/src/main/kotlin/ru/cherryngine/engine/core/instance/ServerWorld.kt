package ru.cherryngine.engine.core.instance

/**
 * Маркерный интерфейс серверного мира.
 *
 * Конкретные реализации (`MinecraftServerWorld`, `BedrockServerWorld` и т.п.)
 * живут в платформенных модулях. Глобальные `@Singleton`-сервисы вроде
 * `TerrainCollisionProvider` / `WorldRaycaster` выбирают подходящую реализацию
 * через `canHandle(world)`.
 */
interface ServerWorld
