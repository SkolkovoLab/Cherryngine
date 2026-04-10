# Cherryngine Engine

Ядро для Minecraft-серверов на Kotlin. Пишется с нуля, без Minestom. Ядро — это фреймворк: подключается в проект, к нему добавляются библиотеки с готовыми системами, и на их базе собирается конкретный игровой режим.

## Ключевые принципы

- **Максимальная гибкость.** Всё переопределяемо, никаких жёстких ограничений.
- **Сложное ядро, простые реализации.** Разработчики геймплея пишут максимально простой код.
- **Нет ванилы.** Режимы — перенос других игр в Minecraft (CS, GTA, билд-баттл, RPG и т.д.).
- **Слоистая архитектура.** Слабые разработчики пишут декларативный код, средние — системы, сильные — ядро.

## Архитектура

```
Уровни (контент)         — декларативный DSL
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

Слои общаются через компоненты, а не через прямые вызовы. Геймплейная система не "вызывает" платформенную — она ставит компонент, а платформенная система его обрабатывает.

---

## Модули

### Библиотеки (lib-*)

Автономные модули без зависимости на движок. Можно использовать отдельно — например, для лимбо-сервера, который загружает мир и отправляет чанки вручную.

#### lib-math

Математические примитивы.

- `Vec3D` — 3D вектор (double). Операции: арифметика, normalize, dot, cross, lerp, rotate.
- `Vec3I` — целочисленные координаты.
- `YawPitch` — углы поворота игрока.
- `Cuboid` — AABB (ось-выровненный ограничивающий объём).
- `Transform` — позиция + вращение (кватернион) + масштаб.
- `QRot` — кватернион.

Зависимости: нет.

#### lib-minecraft

Низкоуровневая реализация протокола Minecraft, реестры, примитивы мира. Самый большой модуль (~612 файлов).

**Сеть:**
- `Connection` — управление TCP-соединением через Netty. Состояния: handshake -> login -> configuration -> play.
- `NettyServer` — запуск сервера, управление каналами.
- Пакеты: полный набор clientbound/serverbound для всех состояний протокола.
- Шифрование (RSA + AES), сжатие (zlib).
- `StreamCodec` — типизированная сериализация/десериализация в Netty ByteBuf.

**Кодеки:**
- `Codec<T>` — формат-агностичная сериализация. Поддерживает BinaryTag (NBT), JSON, YAML.
- Комбинаторный паттерн: `StructCodec`, `ListCodec`, `MapCodec`, `UnionCodec`, `OptionalCodec`.
- `Transcoder<D>` — мост между кодеком и конкретным форматом.

**Мир:**
- `Block` — блок с state ID и свойствами.
- `ChunkSection` — 16x16x16 блоков с палитрой.
- `Palette` — трёхуровневое сжатие: SingleValued (0 бит), Indirect (1-8 бит), Direct (9+ бит).
- `ChunkData` — контейнер для отправки чанка клиенту (секции + heightmaps + block entities).
- `LightData` — данные освещения (sky + block light).

**Реестры:**
- `Registries` — центральный объект со статическими и data-driven реестрами.
- Блоки, предметы, биомы, типы измерений, типы сущностей, звуки, атрибуты и т.д.
- `TagRegistry` — система тегов Minecraft (группировка блоков, биомов и т.д.).
- Данные генерируются из файлов Minestom модулем `lib-minecraft:generator` (KotlinPoet).

**Компоненты предметов:**
- ~97 data-классов для компонентов предметов (ArmorTrim, Enchantments, Food, и т.д.).

Зависимости: lib-math, Netty, Kyori Adventure.

#### lib-world

Система слоёв и композиция миров. Минимальный модуль для работы с мирами без движка.

**Интерфейсы:**
- `World` — запросы блоков, секций, освещения, heightmap, block entities. Метод `getChunkData()` собирает `ChunkData` для отправки клиенту.
- `Layer` — composable слой. `getBlock()` возвращает `null` (прозрачный) или `Block`. Свойство `voidMarker` — блок "явного воздуха", вырезающий дыры в нижних слоях.

**Реализации слоёв:**
- `ImmutableLayer` — неизменяемый, загружается из файла. Секции в `Long2ObjectOpenHashMap`. Air (stateId=0) = прозрачный.
- `MutableLayer` — sparse editable. Операции: `setBlock`, `putVoid` (явный воздух), `remove` (наследовать снизу). `putSection` для bulk-загрузки.

**Композиция:**
- `LayeredWorld` — составной мир из слоёв с приоритетами. Алгоритм: сверху вниз по приоритету, первый не-null блок побеждает. VoidMarker -> AIR. Кэширует сортировку слоёв.
- `ImmutableLayerKey` — ключ идентификации набора immutable слоёв (список ID в порядке приоритета).

**Трекинг изменений:**
- `MutableLayerChangeTracker` — отслеживает dirty chunks в mutable слоях. Non-destructive `getDirty()`, `clear()` в конце тика.

**Прочее:**
- `ChunkHeightmap(s)` — heightmap хранилище на `SimpleBitStorage`.
- `LightEngine` — расчёт освещения (WIP).
- `VisibleBarriersWorld` — debug обёртка, показывает барьеры как красное стекло.

Зависимости: lib-minecraft.

#### lib-polar

Загрузка миров из формата Polar (компактный бинарный формат с ZSTD-сжатием).

- `PolarWorld`, `PolarChunk`, `PolarSection` — in-memory представление.
- `PolarReader` — десериализация из байтов.
- `PolarWorldGenerator` — загрузка в `ImmutableLayer` (`loadAsLayer`) и `MutableLayer` (`loadAsMutableLayer`).
- `PolarDataConverter` — миграция версий данных Minecraft.

Зависимости: lib-minecraft, lib-world, zstd-jni.

#### lib-jackson

Jackson-сериализация с кастомными процессорами для игровых типов.

- `JacksonModule` — Micronaut factory: `JsonMapper`, `YAMLMapper`.
- `JacksonSerializationProcessor` — плагин для типоспецифичной сериализации.
- Реализации: Vec3D, Transform, QRot, Key, MiniMessage, Cuboid и др.
- `CommentsYamlMapper` — YAML с сохранением комментариев.

Зависимости: Jackson, lib-math, Kyori Adventure.

#### lib-adventure-serializer-nbt

Мост между Kyori Adventure компонентами и Minecraft NBT. Auto-service плагин.

#### lib-viaversion

Обёртка над ViaVersion/ViaBackwards для поддержки нескольких версий протокола.

---

### Движок (engine-*)

Модули, составляющие серверный фреймворк. Используют Micronaut DI.

#### engine-core

Высокоуровневый серверный фреймворк.

**Запуск и конфигурация:**
- `Main` — точка входа, запуск Micronaut.
- `CherryngineRunner` — слушает `StartupEvent`, запускает `NettyServer`.
- `EngineCoreConfig` — адрес, порт, mojangAuth, compressionThreshold.

**Игроки:**
- `Player` — подключённый игрок: connection, game profile, позиция, видимость.
- `PlayerManager` (@Singleton) — управление игроками: создание при логине, обновление позиций, очередь пакетов, синхронизация реестров, async configuration через virtual threads.

**Connection vs Entity (критично!):** В ядре нет класса Player как сущности. `Connection` — TCP-сокет, живёт на gateway-слое. Entity внутри Instance — обычная ECS entity с компонентами (`PlayerComponent(uuid)`, `PositionComponent`). Связь через компоненты.

**События (Micronaut ApplicationEventPublisher):**
- `ConnectEvent`, `DisconnectEvent` — жизненный цикл соединения.
- `PacketEvent` — приём пакетов.
- `PlayerCreatedEvent`, `PlayerConfigurationAsyncEvent` — жизненный цикл игрока.
- `SetGameProfileEvent` — кастомизация профиля.

**Команды:**
- `CloudCommandManager` — интеграция с Cloud command framework.
- `@CloudCommand` — аннотация для регистрации команд.
- Парсеры аргументов: Location, Player, Key.
- Brigadier интеграция.

**Оптимизация миров:**
- `ChunkPool` (@Singleton) — кэш скомпонованных `ChunkData` для наборов immutable слоёв. Ключ: `ImmutableLayerKey` + `ChunkPos`. `computeIfAbsent` — каждый чанк компонуется один раз.
- `LayerClassification` — разделяет слои на immutable/mutable с предвычисленным ключом. Один раз на игрока за тик.
- `MutableOverlay` — вычисляет diff между полной композицией (все слои) и immutable base (из ChunkPool). Результат — encoded block changes для `SectionBlocksUpdatePacket`.

**View система:**
- `Viewable`, `ViewableProvider` — интерфейсы отображаемых объектов.
- `BlocksViewable` — отображение блоков в чанке.
- `StaticViewableProvider` — фиксированный набор viewable.

**Утилиты:**
- `StableTicker` — стабильный тикер с компенсацией задержек.

Зависимости: lib-minecraft, lib-world, lib-jackson, Micronaut, Kyori Adventure, Cloud commands, Guava, kotlinx.coroutines.

#### engine-ecs

ECS-интеграция на базе Fleks.

**Типы:**
- `EcsWorld` = Fleks World, `EcsEntity` = Fleks Entity.
- `EcsComponent<T>` — маркер компонента.

**Базовые компоненты:**
- `PlayerComponent` — UUID + viewContextIDs (подписки на слои).
- `PositionComponent` — позиция + YawPitch.
- `ViewableComponent` — viewContextIDs для определения видимости.

**События (компоненты-события, очищаются каждый тик):**
- `PacketsEvent` — очередь входящих пакетов.
- `ViewableProvidersEvent` — слои + viewable providers для ViewSystem.
- `LastPlayerPositionEvent` — предыдущая позиция.

**Базовые системы:**
- `ReadClientPositionSystem` — Player.clientPosition -> PositionComponent.
- `WriteClientPositionSystem` — PositionComponent -> пакеты клиенту (телепорт/relative move).
- `ViewSystem` — основная система отправки мира клиенту:
  - Собирает слои из ViewableProvidersEvent по viewContextIDs игрока.
  - Классифицирует слои (immutable/mutable).
  - Immutable base из ChunkPool -> LevelChunkWithLightPacket.
  - Mutable overlay -> SectionBlocksUpdatePacket.
  - Трекинг отправленных чанков, обработка dirty chunks.
- `CommandActionsSystem` — выполнение команд из очереди.
- `ClearEventsSystem` — очистка событий в конце тика.
- `PacketLogSystem` — debug-логирование пакетов.

**Порядок систем в тике:**
```
1. ReadClientPosition  — чтение ввода
2. CommandActions       — команды
3. [Геймплейные системы] — логика
4. [World/Layer системы] — подготовка данных мира
5. ViewSystem           — отправка клиенту
6. WriteClientPosition  — отправка позиций
7. ClearEvents          — очистка
```

Зависимости: engine-core, Fleks 2.12.

#### engine-physics

Физика на базе Jolt (через JNI).

- `PhysicsSpace` — управление Jolt PhysicsSystem. Слои: moving/non-moving. Гравитация: -17 (Minecraft-adjusted).
- `PhysicsBody` — обёртка над Jolt Body. Трекинг трансформа.
- `JoltLoader` — загрузка нативной библиотеки (Windows64, Linux64).
- `ChunkMesher` — генерация физических мешей из блоков чанков.

Зависимости: engine-core, Jolt JNI, JOML.

#### engine-integration:grim

Интеграция с GrimAC (античит).

- Адаптеры команд, менеджеров, PacketEvents.
- Stub-реализации интерфейсов GrimAC.

#### engine-integration:viaversion

Мост к ViaVersion для мультиверсионности протокола.

---

## Система слоёв (Layer System)

Самая нетривиальная часть ядра. Поддерживает:
- **Билд-баттл**: у каждого игрока свой холст на одних координатах.
- **Квартиры (GTA Online)**: игрок в квартире видит улицу через окно, снаружи квартира не видна.
- **Обычные арены**: один слой, все всех видят.

### Концепция

**Layer** — единица контента с блоками. Типы:
- **Immutable** — загружается из файла, не меняется. Палитра + chunk sections.
- **Mutable** — sparse, записываемый. Для динамического контента.

### Подписки и видимость

У игрока есть `viewContextIDs: Set<String>`. У каждой layer-entity есть `ViewableComponent(viewContextIDs)`. ViewSystem собирает слои, viewContextIDs которых пересекаются с подписками игрока.

### Семантика блоков в слое

- `null` — слой не определяет блок, наследуй из нижнего.
- `voidMarker` (по умолчанию structure_void) — явный воздух, НЕ наследуй.
- Air (stateId=0) — прозрачный, как null.
- Любой другой блок — конкретный блок.

### Композиция

```
для каждого слоя сверху вниз по приоритету:
    block = layer.getBlock(pos)
    если block != null:
        если block == voidMarker -> AIR
        иначе -> block
    иначе -> следующий слой
если ни один слой не определил -> AIR
```

### Оптимизация отправки (ChunkPool + MutableOverlay)

1. Слои классифицируются на immutable и mutable (`LayerClassification`).
2. Immutable композиция кэшируется в `ChunkPool` (ключ: набор слоёв + ChunkPos).
3. Immutable base отправляется как `LevelChunkWithLightPacket`.
4. Mutable overlay вычисляется как diff с immutable base (`MutableOverlay`).
5. Diff отправляется как `SectionBlocksUpdatePacket`.

При смене подписок (вход в квартиру) — новые чанки из пула + overlay. Игроки с одинаковым набором immutable слоёв делят один кэш.

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
- **Сеть:** Netty
- **Физика:** Jolt (через JNI)
- **Сериализация:** kotlinx.serialization, Jackson (JSON/YAML), NBT
- **Команды:** Cloud command framework + Brigadier
- **Текст:** Kyori Adventure (MiniMessage)
- **Мультиверсия:** ViaVersion / ViaBackwards
- **Античит:** GrimAC
- **Формат миров:** Polar (ZSTD)

## Открытые вопросы

1. **Освещение в композированных чанках.** Как эффективно считать свет для микса слоёв.
2. **Smart resubscribe.** Минимизация пакетов при смене подписок на слои.
3. **Физика по персональному виду мира.** Коллизии с блоками, которые видит конкретный игрок.
4. **Тики с разной частотой.** Нужны ли инстансам разные TPS.
5. **DSL для разработчиков уровней.** Kotlin scripting, конфиги или билдеры.
6. **Gateway для distributed-режима.** Где живут Connections, как проксируются пакеты.
7. **Глобальные сервисы.** Профили, статистика, экономика, чат между инстансами.
