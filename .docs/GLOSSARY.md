# Cherryngine — Glossary

## Instance
Изолированный игровой контекст — лобби, матч, арена, квартира. Тикает независимо. Имеет собственные ECS-мир, физику, игроков. Создаётся из `InstancePrefab` через `InstanceFactory`.

## InstancePrefab
Шаблон инстанса. Описывает платформы, миры и список ECS систем. Из одного префаба можно создать несколько независимых инстансов.

## Connection
Глобальный сетевой объект. Живёт пока клиент физически подключён. Только `sendPacket(ServerPacket)` и идентификатор. Не знает про игровую логику.

## Player
Per-instance игровой объект. Создаётся при входе в инстанс, уничтожается при выходе. Хранит игровое состояние (sentChunks, clientPosition и т.д.). Ссылается на `Connection`.

## TransferData
Плоская карта `Map<String, Any>` передаваемая при переходе игрока между инстансами. Например: `{"team": "ct", "matchId": "abc123"}`.

## ServerWorld
Маркерный интерфейс серверного мира в `engine-core`. Конкретные реализации (`MinecraftServerWorld`) — в платформенных модулях. Глобальные сервисы вроде `TerrainCollisionProvider`/`WorldRaycaster` выбирают реализацию через `canHandle(world)`.

## Layer (слой)
Единица контента с блоками. `ImmutableLayer` — загружается из Polar файла. `MutableLayer` — sparse, записываемый. Несколько слоёв композируются в `MinecraftServerWorld` под `contextID`-ом.

## viewContextIDs
Набор идентификаторов контекстов, которые видит игрок. Определяет какие слои мира отправляются клиенту и какие entity он видит.

## physContextIDs
Набор идентификаторов контекстов для физических коллизий тела. Тело сталкивается только с terrain и другими телами из пересекающихся контекстов.

## InstanceSingletonScope
Custom Micronaut scope для `@InstanceSingleton`-бинов. Хранит активный `Instance` в `ThreadLocal`. Резолв вне `Instance.get()` бросает ошибку.

## Renderer
Per-instance объект, отвечающий за визуальное представление entity на конкретной платформе. Не знает про ECS. Принимает чистые данные (position, name и т.д.) от ECS-системы.

## Tickable
Объект который тикается инстансом каждые N миллисекунд. Примеры: `EcsWorldTickable`, `MinecraftViewTickable`, `MinecraftCommandTickable`. Регистрируется как `@InstanceSingleton(stage = TickStage.X)`.

## TickStage
Порядок исполнения tickables в кадре: `PRE`, `GAME`, `POST`. Внутри стадии порядок не гарантирован — дополнительной упорядоченности добиваются через ECS-системы.

## PlayerPositionSource
Источник «желаемой» позиции игрока для платформенного sync-механизма. `canHandle(player) + getDesired(player) + acceptClientMovement(player, pos, yaw)`. `@InstanceSingleton`, инжектится в `PlayerPositionPre/PostSyncTickable` как `List<PlayerPositionSource>` — паттерн аналогичен диспетчеру рендереров. Реализации: `EcsPlayerPositionSource` (через `PositionComponent`), возможны и другие для мод'ов без ECS.

## Registries
`net.minestom.server.registry.Registries` — Minestom-овский набор `DynamicRegistry`-и (Biome, DimensionType, ChatType, DamageType и т.д.). Используется как `@Singleton`-бин (через `MinecraftServer.updateProcess()` в `MinecraftModule`). Никаких сокетов и тиков Minestom-а при этом не запускается.

## TerrainCollisionProvider
Платформенный провайдер коллизий блока для физики. `canHandle(world)` + `getCollisionCuboids(pos, world, contextIDs): List<Cuboid>` + `getSurfaceProperties(...)`. Реализации: `@Singleton`-ы в платформенных модулях. `engine-physics` инжектит `List<TerrainCollisionProvider>` без зависимости на конкретные платформы.

## WorldRaycaster
Аналогично — платформенный raycaster по блокам мира, возвращает `RaycastHit(hitPos, blockPos, blockMaterial: String)`.
