# Cherryngine Engine

Ядро для игровых серверов на Kotlin. Пишется с нуля, без Minestom. Ядро — это **фреймворк**: подключается в проект, к нему добавляются библиотеки с готовыми системами, и на их базе собирается конкретный игровой режим.

Изначально разрабатывалось для Minecraft-серверов, но архитектура намеренно протокол-независимая — в одном инстансе могут одновременно играть игроки с разных платформ (Minecraft, Hytale, и т.д.).

---

## Ключевые принципы

- **Максимальная гибкость.** Всё переопределяемо, никаких жёстких ограничений.
- **Сложное ядро, простые реализации.** Вся сложность — в ядре, разработчики геймплея пишут простой код.
- **Нет ванилы.** Режимы — перенос других игр (CS, GTA, билд-баттл, RPG и т.д.). Redstone, mob AI, генерация мира — не поддерживаются.
- **Платформа первична, ECS — адаптер поверх неё.** ECS системы не знают про Minecraft. Платформенная логика живёт в отдельных сервисах и Tickable.
- **Слои общаются через компоненты, а не через прямые вызовы.** Геймплейная система не "вызывает" платформенную — она ставит компонент, платформенная система его обрабатывает.
- **Никаких глобальных синглтонов с игровым состоянием.** Всё состояние принадлежит конкретному Instance.
- **Events — не основной механизм коммуникации.** Если тянет сделать ивент — подумай, не лучше ли компонент + система.
- **Простота для геймплейщика важнее элегантности ядра.**

---

## Архитектурные слои

```
Уровни (контент)         — декларативный DSL, пишут слабые разработчики
|
Геймплейные системы      — WeaponSystem, AbilitySystem (ECS)
|
Базовые ECS системы      — MovementSystem, DamageSystem
|
Платформенные сервисы    — WorldService, PlayerService (НЕ ECS)
|
Protocol API             — абстракции: Player, Tickable, InstanceSetup
|
Реализации протокола     — MinecraftPlayer, MinecraftViewTickable и т.д.
```

---

## Instance — единица изоляции

**`Instance`** — изолированный игровой контекст (сессия, матч, лобби, арена). Принимает список `Tickable` и тикает их в правильном порядке:

```kotlin
// engine-core
class Instance(
    val tickDuration: Duration,
    val tickables: List<Tickable>,
) : AutoCloseable {
    fun start() { ... }
    fun stop() { ... }
}

// Типичная сборка:
Instance(
    tickDuration = 50.milliseconds,
    tickables = listOf(
        EcsWorldTickable(ecsWorld),          // ECS тик — платформо-независимый
        MinecraftViewTickable(...),           // Minecraft тик — отправка чанков, entity
    )
)
```

**Следствия:**

1. **Нет глобального состояния.** `PlayerManager` хранит `Player` интерфейс, не игровую сущность.
2. **Тики независимы.** Один инстанс лагает — другие не страдают.
3. **Per-instance объекты** (`McEntityRegistry`, фабрики view) создаются через `InstanceSetupFactory.create()`, не через DI.

---

## Кроссплатформенность

**Принцип:** один инстанс может обслуживать игроков с разных платформ одновременно.

**`Player`** в `engine-core` — интерфейс:
```kotlin
interface Player {
    val uuid: UUID
    val username: String
    fun sendMessage(message: Component)
}
```

Реализации: `MinecraftPlayer`, `HytalePlayer` (гипотетически).

**`WorldService`** — диспетчер с паттерном handler:
```kotlin
// engine-core
class WorldService(private val handlers: List<WorldServiceHandler>) {
    fun setPlayerContext(contextIDs: Set<String>, player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.setPlayerContext(player.uuid, contextIDs)
    }
}

interface WorldServiceHandler {
    fun canHandle(player: Player): Boolean  // проверяется через instanceof
    fun setPlayerContext(uuid: UUID, contextIDs: Set<String>)
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
```

`@Singleton` реализации регистрируются в Micronaut DI — `WorldService` получает их список автоматически. Новая платформа = новый `@Singleton`, ничего не меняется.

**View для entity** — Composite паттерн:
```kotlin
// AxolotlView делегирует всем платформам
class CompositeAxolotlView(private val views: List<AxolotlView>) : AxolotlView {
    override fun updatePosition(position: Vec3D, yawPitch: YawPitch) =
        views.forEach { it.updatePosition(position, yawPitch) }
    // ...
}
```

Аксолотль виден и Minecraft-игроку и Hytale-игроку одновременно — каждый получает пакеты своей платформы.

---

## InstanceSetup — per-instance платформенные ресурсы

`McEntityRegistry`, фабрики view и платформенные Tickable должны быть **per-instance**, не `@Singleton`. Для этого используется паттерн `InstanceSetupFactory`:

```kotlin
// engine-core
interface InstanceSetup {
    fun createTickables(): List<Tickable>
}

interface InstanceSetupFactory<T : InstanceSetup> {
    fun create(): T
}
```

```kotlin
// impl-demo
interface DemoInstanceSetup : InstanceSetup {
    val axolotlViewFactory: AxolotlViewFactory
    val cubeViewFactory: CubeViewFactory
}

interface DemoInstanceSetupFactory : InstanceSetupFactory<DemoInstanceSetup>
```

```kotlin
// impl-demo:minecraft — @Singleton фабрика создаёт per-instance setup
@Singleton
class MinecraftDemoInstanceSetupFactory(...) : DemoInstanceSetupFactory {
    override fun create() = MinecraftDemoInstanceSetup(...) // создаёт McEntityRegistry внутри
}
```

`DemoInit` получает `List<DemoInstanceSetupFactory>` через DI — по одной на каждую платформу. Вызывает `create()` для получения per-instance объектов.

---

## ECS — основа геймплейного слоя

ECS **не знает про Minecraft**. Реализация — **Fleks 2.12**.

**Ключевые принципы:**

- **Data-only компоненты.** `data class`, без лямбд, ссылок на `File`/`Socket`/`Thread`. Поведение — только в системах.
- **Системы не хранят состояние в полях класса.** Всё состояние между тиками — в компонентах. Нарушение ломает сериализацию инстансов.
- **Явный порядок систем.** Никаких "приоритетов слушателей".
- **Реактивность — через change detection.** Система запрашивает "дай всех у кого `Health` изменилось", не через подписки.

**Порядок систем в тике:**
```
1. PlayerInitSystem       — join/leave из каналов PlayerManager → ECS entity
2. ReadClientPosition     — позиция клиента → PositionComponent
3. CommandActions         — выполнение команд из очереди
4. [Геймплейные системы]  — игровая логика
5. PhysicsSystem          — симуляция физики
6. ViewContextSyncSystem  — PlayerComponent.viewContextIDs → WorldService
7. WriteClientPosition    — PositionComponent → телепорт клиенту
8. ClearEvents            — очистка event-компонентов
```

**`PlayerIndex`** — O(1) поиск ECS entity по UUID через `FamilyHook`:
```kotlin
val entity = playerIndex.getOrThrow(uuid) // не O(n) поиск!
```

---

## Система слоёв (Layer System)

Нативная поддержка персонального вида мира для каждого игрока без копирования мира. Используется для **визуализации** (`viewContextIDs`) и **физики** (`physContextIDs`).

### Кейсы

- **Квартиры (GTA Online style)**: каждый игрок видит свою квартиру на одних координатах.
- **Билд-баттл**: у каждого игрока свой холст, во время голосования все видят один холст.
- **Обычные арены**: один слой, все всех видят.

### Концепция слоёв

**Layer** — единица контента с блоками:
- **Immutable** — загружается из файла (Polar формат), быстрое чтение.
- **Mutable** — sparse, записываемый. Для динамического контента.

**Семантика блоков:**
- `null` — слой не определяет блок, наследуй из нижнего
- `voidMarker` (по умолчанию `structure_void`) — явный воздух, вырезает дыры в нижних слоях
- Любой другой блок — конкретный блок

**Алгоритм композиции:**
```
для каждого слоя сверху вниз по приоритету:
    block = layer.getBlock(pos)
    если block != null:
        если block == voidMarker -> AIR
        иначе -> block
    иначе -> следующий слой
```

### viewContextIDs — видимость блоков

`PlayerComponent.viewContextIDs` определяет что игрок видит. `ViewContextSyncSystem` синхронизирует это в `WorldService`. `MinecraftWorldServiceHandler` отправляет нужные чанки.

**Оптимизация чанков:**
- Immutable композиция кэшируется в `ChunkPool` (ключ: `ImmutableLayerKey` + `ChunkPos`)
- Immutable base → `LevelChunkWithLightPacket`
- Mutable overlay (diff) → `SectionBlocksUpdatePacket`

### physContextIDs — физические коллизии

`PhysicsComponent.physContextIDs` определяет с какими блоками тело сталкивается. `TerrainGenerator` динамически добавляет/удаляет Jolt-тела из слоёв соответствующих контексту. `ContactListener` фильтрует коллизии между динамическими телами по пересечению контекстов.

---

## Физика (engine-physics)

Реализована на **Jolt Physics** через **jolt-jni**. Один `PhysicsSpace` на инстанс.

- `PhysicsSpace` — Jolt `PhysicsSystem`, heartbeat паттерн (`beginTick`/`keepAlive`/`endTick`) для auto-cleanup тел.
- `TerrainGenerator` — динамические terrain тела. Ключ кэша: `TerrainKey(Vec3I, Set<String>)` — отдельные тела для разных physContextIDs на одних координатах.
- `ContactListener` — фильтрация по пересечению `physContextIDs`.

---

## Модули

### Библиотеки (lib-*)

Автономные модули без зависимости на движок.

#### lib-math
`Vec3D`, `Vec3I`, `YawPitch`, `Cuboid` (AABB), `Transform`, `QRot`.

#### lib-minecraft
Низкоуровневая реализация протокола Minecraft (~612 файлов).

- Сеть: `Connection` (Netty), `NettyServer`, полный набор пакетов, шифрование, сжатие, `StreamCodec`.
- Кодеки: `Codec<T>` (NBT/JSON/YAML), комбинаторные кодеки.
- Мир: `Block`, `ChunkSection`, `Palette`, `ChunkData`, `LightData`.
- Реестры: блоки, предметы, биомы, измерения, звуки, атрибуты, `TagRegistry`.

#### lib-world
- `ImmutableLayer`, `MutableLayer`, `LayeredWorld`, `ImmutableLayerKey`.
- `MutableLayerChangeTracker` — dirty chunks между тиками.
- `LightEngine` (WIP).

#### lib-polar
Загрузка миров из формата Polar (ZSTD). `loadAsLayer`, `loadAsMutableLayer`.

#### lib-jackson
Jackson JSON/YAML с процессорами для игровых типов.

#### lib-adventure-serializer-nbt
Мост Kyori Adventure ↔ Minecraft NBT.

#### lib-viaversion
Обёртка ViaVersion/ViaBackwards.

---

### Движок (engine-*)

#### engine-core
Протокол-независимое ядро. Не зависит от `lib-minecraft`.

- `Player` (интерфейс) — uuid, username, sendMessage.
- `PlayerManager` (@Singleton) — register/unregister/getPlayer, `playerJoinChannel`/`playerLeaveChannel`.
- `PlayerInputProvider`, `PlayerOutputProvider` — интерфейсы для чтения/записи состояния клиента.
- `WorldService` + `WorldServiceHandler` — диспетчер с canHandle(player).
- `PlayerService` + `PlayerServiceHandler` — onPlayerJoin/onPlayerLeave.
- `CommandSender` (интерфейс).
- `Instance` — контейнер с `List<Tickable>`.
- `Tickable` — интерфейс для тикаемых объектов.
- `InstanceSetup` + `InstanceSetupFactory` — паттерн для per-instance ресурсов.
- `StableTicker` — стабильный тикер с компенсацией задержек.

#### engine-ecs
ECS без Minecraft. Зависит только от `engine-core`.

- `EcsWorldTickable` — оборачивает ECS мир в `Tickable`.
- `PlayerIndex` — O(1) поиск ECS entity по UUID через `FamilyHook`.
- Компоненты: `PlayerComponent`, `PositionComponent`, `ViewableComponent`.
- События: `EcsEvent`, `LastPlayerPositionEvent`.
- Системы: `ReadClientPositionSystem(PlayerInputProvider)`, `WriteClientPositionSystem(PlayerOutputProvider)`, `ViewContextSyncSystem(WorldService)`, `CommandActionsSystem`, `ClearEventsSystem`.

#### engine-minecraft
Minecraft-реализация. Не зависит от `engine-ecs`.

- `MinecraftPlayer : Player` — clientPosition, sentChunks, connection.
- `MinecraftConnectionService` — обработка пакетов, onMove, onDisconnect.
- `MinecraftPlayerInputProvider`, `MinecraftPlayerOutputProvider`.
- `MinecraftWorldServiceHandler` (@Singleton) — хранит контексты игроков, слои, отправляет чанки.
- `MinecraftViewTickable` — per-instance (не @Singleton), отправка блоков и entity без ECS.
- `McEntity`, `McEntityRegistry` — per-instance (не @Singleton).
- `ChunkPool` (@Singleton), `LayerClassification`, `MutableOverlay`.
- `Viewable`, `BlocksViewable`, `ViewableProvider`.
- `CloudCommandManager` — Cloud commands + Brigadier + Minecraft пакеты.

#### engine-physics
- `PhysicsSpace` — per-instance, Jolt Physics.
- `TerrainGenerator` — per-instance, динамические terrain тела.
- `TerrainLayerProvider` — интерфейс для предоставления слоёв физике.

---

## Граф зависимостей

```
lib-math
  +-- lib-minecraft
  |     +-- lib-world
  |     |     +-- lib-polar
  |     +-- lib-viaversion
  +-- lib-jackson

lib-adventure-serializer-nbt (standalone)

engine-core                    (зависит от lib-math, Kyori, Cloud commands)
  +-- engine-ecs               (engine-core + Fleks)
  +-- engine-minecraft         (engine-core + lib-minecraft + lib-world + lib-jackson)
  +-- engine-physics           (engine-core + Jolt JNI)
  +-- engine-integration:grim
  +-- engine-integration:viaversion
```

**Ключевое: `engine-ecs` ↔ `engine-minecraft` — нет зависимости в обе стороны.**

---

## Демо-проект (impl-demo)

Демонстрирует архитектуру на практике. Два подмодуля:

### impl-demo (платформо-независимая часть)
- `DemoInit` (@Singleton) — создаёт ECS мир и `Instance`. Получает `List<DemoInstanceSetupFactory>` через DI.
- `GameWorldProvider` (интерфейс) — список имён слоёв.
- `DemoInstanceSetup` + `DemoInstanceSetupFactory` — паттерн per-instance ресурсов.
- `AxolotlView`, `CubeView` — интерфейсы отображения.
- `CompositeAxolotlViewFactory`, `CompositeCubeViewFactory` — делегируют всем платформам.
- Системы: `PlayerInitSystem`, `AxolotlModelSystem`, `CubeModelSystem`, `ApartSystem`, `PhysicsSystem`.

### impl-demo:minecraft (Minecraft реализация)
- `DemoWorlds : GameWorldProvider` — загружает Polar файлы.
- `MinecraftDemoInstanceSetup` — создаёт `McEntityRegistry`, `MinecraftAxolotlViewFactory`, `MinecraftCubeViewFactory`, `MinecraftViewTickable` — все шарят один реестр.
- `MinecraftDemoInstanceSetupFactory` (@Singleton).
- `WorldSystem` — регистрирует слои в `MinecraftWorldServiceHandler`.
- `TestCommand` — команды для тестирования.

---

## Технический стек

- **Язык:** Kotlin
- **DI:** Micronaut (compile-time, без рефлексии)
- **ECS:** Fleks 2.12
- **Сеть:** Netty + протокол Minecraft
- **Мультиверсия:** ViaVersion / ViaBackwards
- **Физика:** Jolt Physics (через jolt-jni)
- **Сериализация:** kotlinx.serialization, Jackson (JSON/YAML), NBT
- **Команды:** Cloud command framework + Brigadier
- **Текст:** Kyori Adventure (MiniMessage)
- **Формат миров:** Polar (ZSTD)
- **Античит:** GrimAC

---

## Открытые вопросы

1. **Освещение в композированных чанках.** Как считать свет для микса слоёв.
2. **Smart resubscribe.** Минимизация пакетов при смене viewContextIDs.
3. **Сложные формы блоков в физике.** Ступеньки, заборы, полублоки.
4. **Тики с разной частотой.** Нужны ли инстансам разные TPS?
5. **DSL для разработчиков уровней.** Kotlin scripting, конфиги или билдеры.
6. **Instance как система сообщений.** Lobby instance → game instance через асинхронные сообщения. Детали не проработаны.
7. **Глобальные сервисы.** Профили, статистика, экономика, чат между инстансами.
8. **Multi-world внутри Instance.** Текущая позиция: через Layer System.

---

## Антипаттерны

**1. ECS система зависит от платформы**
```kotlin
class SomeSystem(playerManager: PlayerManager) // ❌ — PlayerManager из engine-core, но
class SomeSystem(registry: McEntityRegistry)   // ❌ — McEntityRegistry из engine-minecraft
```
ECS системы зависят только от `engine-core` интерфейсов.

**2. Платформенная логика в ECS системе**
```kotlin
class ViewSystem : IteratingSystem() {
    override fun onTickEntity(entity) {
        player.connection.sendPacket(...) // ❌ — пакеты в ECS
    }
}
```
Платформенная логика живёт в `Tickable`, не в ECS.

**3. Per-instance объект как @Singleton**
```kotlin
@Singleton class McEntityRegistry // ❌ — создаётся через InstanceSetupFactory
```

**4. Глобальный singleton с игровым состоянием**
```kotlin
object PlayerManager { fun getPlayer(uuid: UUID): Player? } // ❌
```

**5. Состояние в полях ECS системы**
```kotlin
class WeaponSystem : System() {
    private val cooldowns = mutableMapOf<EntityId, Long>() // ❌ — в компонент
}
```

**6. Логика в компонентах**
```kotlin
data class Health(var current: Int) {
    fun damage(amount: Int) { ... } // ❌
}
```

**7. Реактивность через подписки**
```kotlin
healthComponent.onChange { updateUI(it) } // ❌ — через change detection в системе
```

**8. Прямые ссылки между инстансами**
```kotlin
class Quest(val player: MinecraftPlayer) // ❌ — только UUID
```