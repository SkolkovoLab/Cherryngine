# Cherryngine Engine

Ядро для Minecraft-серверов на Kotlin. Пишется с нуля, без Minestom. Ядро — это **фреймворк**: подключается в проект, к нему добавляются библиотеки с готовыми системами, и на их базе собирается конкретный игровой режим.

---

## Ключевые принципы

- **Максимальная гибкость.** Всё переопределяемо, никаких жёстких ограничений.
- **Сложное ядро, простые реализации.** Вся сложность — в ядре, разработчики геймплея пишут простой код.
- **Нет ванилы.** Режимы — перенос других игр в Minecraft (CS, GTA, билд-баттл, RPG и т.д.). Redstone, mob AI, генерация мира, физика воды — не поддерживаются.
- **Слоистая архитектура.** Слабые разработчики пишут декларативный код, средние — ECS-системы, сильные — ядро.
- **Слои общаются через компоненты, а не через прямые вызовы.** Геймплейная система не "вызывает" платформенную — она ставит компонент, платформенная система его обрабатывает.
- **Никаких глобальных синглтонов с игровым состоянием.** Всё состояние принадлежит конкретному Instance.
- **Events — не основной механизм коммуникации.** Если тянет сделать ивент — подумай, не лучше ли компонент + система.
- **Простота для геймплейщика важнее элегантности ядра.**

---

## Слоистая архитектура

```
Уровни (контент)         — декларативный DSL, пишут слабые разработчики
   |
Геймплейные системы      — WeaponSystem, AbilitySystem
   |
Базовые системы          — MovementSystem, DamageSystem
   |
Платформенные системы    — ViewSystem, ConnectionSystem, WorldSystem
   |
Protocol API             — абстракция над пакетами
   |
Реализации протокола     — адаптеры под конкретные версии
```

---

## Instance — единица изоляции

**Instance** — изолированный игровой контекст (сессия, матч, лобби, арена). Концептуально — актор:

- Собственное состояние, свой тик, свой ECS-мир, один `PhysicsSpace`
- Никто снаружи не лезет внутрь напрямую
- Между инстансами — только асинхронные сообщения
- Нельзя "перенести" entity между инстансами, только удалить из одного и создать в другом
- Потенциально может жить на другой машине (location transparency)

**Следствия:**

1. **Нет глобального состояния.** Никаких `PlayerManager.getPlayer(uuid)` как игровой сущности.
2. **Между Instance — только ID и сериализуемые сообщения.** Никаких прямых ссылок на объекты из другого Instance.
3. **Все сообщения между Instance — `@Serializable`.** Даже если сейчас всё в одном процессе.
4. **Тики Instance независимы.** Один лагает — другие не страдают.
5. **Нет глобальной БД на уровне ядра.** Если Instance нужны сохранения — система внутри него сама общается с БД асинхронно.

---

## Connection vs Player (критично!)

**В ядре нет класса Player как игровой сущности.** Есть три разных концепции:

- **Connection** — TCP-сокет с клиентом, протокольное состояние. Живёт на gateway-слое **вне** инстансов, в `engine-core`.
- **`Player` в `engine-core`** — клиентский стейт: что уже отправлено клиенту, текущее состояние соединения, позиция клиента, видимые чанки. Это не игровая сущность — это состояние соединения.
- **Entity внутри Instance** — обычная ECS entity с компонентами (`PlayerComponent(uuid)`, `PositionComponent`). Это игровое представление персонажа.

Связь между Connection и ECS entity — через UUID. Connection-уровневые события (`ConnectEvent`, `DisconnectEvent`, `PacketEvent`) — глобальные через Micronaut event bus, так как Connection живёт глобально вне инстансов.

**Почему это важно:** радикальное отличие от Bukkit/Spigot, где Player — и сущность, и подключение. Разделение даёт reconnect, спектатор-режимы, distributed-архитектуру.

---

## ECS — основа геймплейного слоя

Внутри Instance всё состояние живёт в ECS. Реализация — **Fleks** (нативный Kotlin ECS).

**Ключевые принципы:**

- **Data-only компоненты.** Компоненты — `data class`, без лямбд, ссылок на внешние ресурсы, `File`/`Socket`/`Thread`. Поведение — только в системах. Это необходимо для будущей сериализации инстансов.
- **Системы не хранят состояние в полях класса.** Всё состояние между тиками — в компонентах или resources. Нарушение ломает сериализацию инстансов.
- **Явный порядок систем внутри тика.** Никаких "приоритетов слушателей", никаких глобальных ивентов как основного механизма.
- **Порядок событий — через фазы тика.** Все источники урона собираются → применяются модификаторы → суммируется → проверяется смерть. Всё за один тик, детерминированно.
- **Реактивность — через change detection.** Система запрашивает "дай всех у кого `Health` изменилось", не через подписки.

**Текущий порядок систем в тике:**
```
1. ReadClientPosition    — чтение входящих позиций клиента
2. CommandActions        — выполнение команд
3. [Геймплейные системы] — игровая логика
4. PhysicsSystem         — симуляция физики
5. [World/Layer системы] — подготовка данных мира
6. ViewSystem            — отправка блоков клиенту
7. WriteClientPosition   — отправка позиций клиенту
8. ClearEvents           — очистка event-компонентов
```

---

## Система слоёв (Layer System)

Нативная поддержка персонального вида мира для каждого игрока. Используется для двух независимых задач: **визуализация** (`viewContextIDs`) и **физика** (`physContextIDs`).

### Кейсы

- **Билд-баттл**: у каждого игрока свой холст на одних координатах, во время голосования все смотрят один холст.
- **Квартиры (GTA Online style)**: игрок в своей квартире видит улицу через окно, но снаружи квартира не видна. У каждого игрока своя квартира на одном и том же месте.
- **Обычные арены**: один слой, все всех видят.

### Концепция слоёв

**Layer** — единица контента с блоками. Типы:
- **Immutable** — загружается из файла (Polar формат), не изменяется. Быстрое чтение.
- **Mutable** — sparse, записываемый. Для динамического контента: квартиры, разрушаемые объекты.

Свойства слоя: `id`, `priority` (порядок наложения), `voidMarker` (блок "явного воздуха", по умолчанию `structure_void`).

**Семантика блоков:**
- `null` — слой не определяет блок в этой точке, наследуй из нижнего слоя
- `voidMarker` — явный воздух, НЕ наследуй ничего снизу (вырезает дыры)
- Air (stateId=0) — прозрачный, то же что null
- Любой другой блок — конкретный блок

**Алгоритм композиции:**
```
для каждого слоя сверху вниз по приоритету:
    block = layer.getBlock(pos)
    если block != null:
        если block == voidMarker -> AIR
        иначе -> block
    иначе -> следующий слой
если ни один слой не определил -> AIR
```

### viewContextIDs — видимость блоков

У каждого игрока (ECS entity) есть `PlayerComponent.viewContextIDs: Set<String>`. У каждого слоя (layer-entity) есть `ViewableComponent(viewContextIDs)`. `ViewSystem` собирает слои, чьи `viewContextIDs` пересекаются с подписками игрока.

**Смена подписок:** при входе в квартиру `ApartSystem` меняет `viewContextIDs` игрока — `ViewSystem` автоматически отправляет новые чанки.

**Оптимизация отправки чанков:**
- Слои классифицируются на immutable и mutable (`LayerClassification`).
- Immutable композиция кэшируется в `ChunkPool` (ключ: `ImmutableLayerKey` + `ChunkPos`). Игроки с одинаковым набором immutable слоёв делят один кэш.
- Immutable base → `LevelChunkWithLightPacket`.
- Mutable overlay (diff) → `SectionBlocksUpdatePacket`.
- При смене `viewContextIDs` инвалидируется только та часть кэша где изменился immutable base (по `ImmutableLayerKey`). Mutable overlay всегда дифф.

**Клиентский стейт чанков** хранится в `Player`:
- `sentChunksBase: ImmutableLayerKey?` — для какого immutable base отправлены чанки
- `sentChunks: MutableSet<ChunkPos>` — какие чанки уже есть у клиента

### physContextIDs — физические коллизии

У каждого физического тела (`PhysicsComponent`) есть `physContextIDs: Set<String>`. Определяет с какими блоками и объектами тело сталкивается.

**Terrain generation (по образцу Rayon):**
Каждый тик `TerrainGenerator` динамически добавляет/удаляет статичные Jolt-тела (блоки) вокруг каждого активного физического объекта. Блоки берутся из `LayeredWorld` построенного из слоёв соответствующих `physContextIDs` тела. Предмет Васяна получает слои `["street", "apt_vasya"]` — его стол физически существует. Предмет Петяна получает `["street", "apt_petya"]` — стол на том же месте другой.

**Фильтрация коллизий между объектами:**
Один `PhysicsSpace` на весь инстанс. `ContactListener` в Jolt фильтрует коллизии между динамическими телами: два тела сталкиваются только если у них есть хотя бы один общий physContext. Terrain тела (NON_MOVING) фильтруются на уровне TerrainGenerator — они уже построены из правильных слоёв.

---

## Физика (engine-physics)

Реализована на **Jolt Physics** через **jolt-jni**.

**Архитектура:**
- Один `PhysicsSpace` на инстанс.
- `PhysicsSpace` — управление Jolt `PhysicsSystem`. ObjectLayers: `MOVING` (динамические тела), `NON_MOVING` (terrain, статичные объекты). Гравитация: -17 (Minecraft-adjusted).
- `TerrainGenerator` — динамическое добавление/удаление terrain тел из слоёв по `physContextIDs` активных тел.
- `TerrainBody` — статичное Jolt тело представляющее один блок.
- `ContactListener` — фильтрация коллизий по пересечению `physContextIDs`.

**Форма блоков:** `BoxShape(0.5, 0.5, 0.5)` для простых блоков. Сложные формы (ступеньки, заборы, полублоки) — открытый вопрос.

Зависимости: engine-core, Jolt JNI.

---

## Модули

### Библиотеки (lib-*)

Автономные модули без зависимости на движок.

#### lib-math
Математические примитивы: `Vec3D`, `Vec3I`, `YawPitch`, `Cuboid` (AABB), `Transform`, `QRot`.

#### lib-minecraft
Низкоуровневая реализация протокола Minecraft, реестры, примитивы мира (~612 файлов).

- **Сеть:** `Connection` (Netty, состояния handshake→login→configuration→play), `NettyServer`, полный набор пакетов, шифрование (RSA+AES), сжатие (zlib), `StreamCodec`.
- **Кодеки:** `Codec<T>` (NBT/JSON/YAML), `StructCodec`, `ListCodec`, `MapCodec`, `UnionCodec`, `OptionalCodec`.
- **Мир:** `Block`, `ChunkSection` (16x16x16 с палитрой), `Palette` (SingleValued/Indirect/Direct), `ChunkData`, `LightData`.
- **Реестры:** `Registries`, блоки/предметы/биомы/измерения/звуки/атрибуты, `TagRegistry`. Данные генерируются из файлов Minestom через `lib-minecraft:generator` (KotlinPoet).
- **Компоненты предметов:** ~97 data-классов (ArmorTrim, Enchantments, Food и т.д.).

Зависимости: lib-math, Netty, Kyori Adventure.

#### lib-world
Система слоёв и композиция миров.

- `World` — интерфейс: запросы блоков, секций, освещения, heightmap, block entities. `getChunkData()` собирает `ChunkData`.
- `Layer` — composable слой. `getBlock()` возвращает `null` или `Block`. `voidMarker` — "явный воздух".
- `ImmutableLayer` — неизменяемый, секции в `Long2ObjectOpenHashMap`.
- `MutableLayer` — sparse editable. `setBlock`, `putVoid`, `remove`, `putSection`.
- `LayeredWorld` — составной мир из слоёв с приоритетами.
- `ImmutableLayerKey` — ключ идентификации набора immutable слоёв.
- `MutableLayerChangeTracker` — отслеживает dirty chunks. Non-destructive `getDirty()`, `clear()` в конце тика.
- `LightEngine` — расчёт освещения (WIP).

Зависимости: lib-minecraft.

#### lib-polar
Загрузка миров из формата Polar (бинарный, ZSTD-сжатие).

- `PolarReader` — десериализация из байтов.
- `PolarWorldGenerator` — загрузка в `ImmutableLayer` (`loadAsLayer`) и `MutableLayer` (`loadAsMutableLayer`).
- `PolarDataConverter` — миграция версий данных Minecraft.

Зависимости: lib-minecraft, lib-world, zstd-jni.

#### lib-jackson
Jackson-сериализация для игровых типов. `JsonMapper`, `YAMLMapper`, `CommentsYamlMapper`. Кастомные процессоры: Vec3D, Transform, QRot, Key, MiniMessage, Cuboid.

#### lib-adventure-serializer-nbt
Мост между Kyori Adventure компонентами и Minecraft NBT.

#### lib-viaversion
Обёртка над ViaVersion/ViaBackwards для поддержки нескольких версий протокола.

---

### Движок (engine-*)

Модули серверного фреймворка. Используют Micronaut DI (compile-time, без рефлексии).

#### engine-core
Высокоуровневый серверный фреймворк.

- `Main` + `CherryngineRunner` — точка входа, запуск Netty сервера.
- `EngineCoreConfig` — адрес, порт, mojangAuth, compressionThreshold.
- `Player` — клиентский стейт: connection, game profile, позиция клиента, `sentChunks`, `sentChunksBase`.
- `PlayerManager` (@Singleton) — lifecycle игроков через `Channel<UUID>` (join/leave), очередь пакетов через `Channel<Pair<UUID, Packet>>`. Thread-safe мост между Netty и ECS тиком.
- Connection events (Micronaut): `ConnectEvent`, `DisconnectEvent`, `PacketEvent`, `PlayerCreatedEvent`, `PlayerConfigurationAsyncEvent`, `SetGameProfileEvent`.
- `CloudCommandManager` — Cloud command framework + Brigadier интеграция.
- `ChunkPool` (@Singleton) — кэш `ChunkData` для immutable слоёв. Ключ: `ImmutableLayerKey` + `ChunkPos`.
- `LayerClassification` — разделение слоёв на immutable/mutable с предвычисленным `ImmutableLayerKey`.
- `MutableOverlay` — diff между полной композицией и immutable base.
- `StableTicker` — стабильный тикер с компенсацией задержек.

Зависимости: lib-minecraft, lib-world, lib-jackson, Micronaut, Kyori Adventure, Cloud commands, kotlinx.coroutines.

#### engine-ecs
ECS-интеграция на базе Fleks 2.12.

**Компоненты:**
- `PlayerComponent` — UUID + `viewContextIDs`.
- `PositionComponent` — позиция + YawPitch.
- `ViewableComponent` — `viewContextIDs` слоя.

**Event-компоненты (очищаются в конце тика):**
- `PacketsEvent` — входящие пакеты.
- `ViewableProvidersEvent` — слои + viewable providers.
- `LastPlayerPositionEvent` — предыдущая позиция.

**Системы:**
- `ReadClientPositionSystem`, `WriteClientPositionSystem`.
- `ViewSystem` — отправка блоков клиенту по viewContextIDs. Immutable base из ChunkPool, mutable overlay как diff.
- `CommandActionsSystem`, `ClearEventsSystem`, `PacketLogSystem`.

Зависимости: engine-core, Fleks 2.12.

#### engine-physics
Физика на базе Jolt через jolt-jni.

- `PhysicsSpace` — Jolt `PhysicsSystem`, `ContactListener` (фильтрация по physContextIDs), управление телами.
- `TerrainGenerator` — динамическое добавление/удаление terrain тел из слоёв по physContextIDs активных тел.
- `TerrainBody` — статичное Jolt тело представляющее один блок.
- `JoltLoader` — загрузка нативных библиотек (Windows64, Linux64).

Зависимости: engine-core, Jolt JNI.

#### engine-integration:grim
Интеграция с GrimAC (античит). Адаптеры команд, менеджеров, PacketEvents, stub-реализации интерфейсов.

#### engine-integration:viaversion
Мост к ViaVersion для мультиверсионности протокола.

---

## Граф зависимостей

```
lib-math
  |
  +-- lib-minecraft
  |     |
  |     +-- lib-world
  |     |     |
  |     |     +-- lib-polar
  |     |
  |     +-- lib-viaversion
  |
  +-- lib-jackson

lib-adventure-serializer-nbt (standalone)

engine-core
  +-- lib-minecraft, lib-world, lib-jackson
  |
  +-- engine-ecs
  |     +-- engine-core, Fleks
  |
  +-- engine-physics
  |     +-- engine-core, Jolt JNI
  |
  +-- engine-integration:grim
  |     +-- engine-core
  |
  +-- engine-integration:viaversion
        +-- engine-core, lib-viaversion
```

---

## Технический стек

- **Язык:** Kotlin
- **DI:** Micronaut (compile-time, без рефлексии)
- **ECS:** Fleks 2.12
- **Сеть:** Netty + собственная реализация протокола Minecraft 1.21.4
- **Мультиверсия:** ViaVersion / ViaBackwards
- **Физика:** Jolt Physics (через jolt-jni)
- **Сериализация:** kotlinx.serialization, Jackson (JSON/YAML), NBT
- **Команды:** Cloud command framework + Brigadier
- **Текст:** Kyori Adventure (MiniMessage)
- **Формат миров:** Polar (ZSTD)
- **Античит:** GrimAC

---

## Открытые вопросы

1. **Освещение в композированных чанках.** Как считать свет для микса слоёв. Варианты: финальный микс с кэшированием, свет базового слоя с артефактами, гибрид.

2. **Smart resubscribe.** Минимизация пакетов при смене viewContextIDs. Diff или полный resend.

3. **Сложные формы блоков в физике.** Сейчас все блоки — `BoxShape(0.5)`. Ступеньки, заборы, полублоки требуют отдельного решения.

4. **Тики с разной частотой.** Нужны ли инстансам разные TPS (лобби 5, матч 20)?

5. **DSL для разработчиков уровней.** Kotlin scripting, декларативные конфиги или билдеры.

6. **Gateway для distributed-режима.** Где живут Connections, как проксируются пакеты между gateway и instance на другой машине.

7. **Глобальные сервисы.** Профили, статистика, экономика, чат между инстансами — отдельные сервисы с асинхронным API.

8. **Multi-world внутри Instance.** Могут ли в одном Instance быть несколько независимых пространств блоков. Текущая позиция: через Layer System. Вводить `World` как отдельный концепт — только если слоёв окажется недостаточно.

---

## Антипаттерны

**1. Глобальный singleton с игровым состоянием**
```kotlin
object PlayerManager { fun getPlayer(uuid: UUID): Player? } // ❌
```
Ломает изоляцию, тестируемость, distributed deployment.

**2. События как основной механизм коммуникации**
```kotlin
eventBus.publish(PlayerDamageEvent(player, 10)) // ❌
// → entity[PendingDamage] = PendingDamage(amount=10) // ✓
```

**3. Логика в компонентах**
```kotlin
data class Health(var current: Int) {
    fun damage(amount: Int) { ... } // ❌ логика в данных
}
```

**4. Прямые ссылки на entity между Instance**
```kotlin
class Quest(val player: Player) // ❌ — только ID
```

**5. Состояние в полях системы**
```kotlin
class WeaponSystem : System() {
    private val cooldowns = mutableMapOf<EntityId, Long>() // ❌ — в компонент
}
```

**6. Реактивность через подписки**
```kotlin
healthComponent.onChange { newValue -> updateUI(newValue) } // ❌ — через change detection
```