# Cherryngine — Architecture

## Технический стек

- **Язык:** Kotlin
- **DI:** Micronaut (compile-time, без рефлексии)
- **ECS:** Fleks (опционально)
- **Сеть:** Netty + собственная реализация протокола Minecraft
- **Мультиверсия:** ViaVersion / ViaBackwards
- **Физика:** Jolt Physics (через jolt-jni)
- **Команды:** Cloud command framework + Brigadier
- **Текст:** Kyori Adventure (MiniMessage)
- **Формат миров:** Polar (ZSTD)

---

## Модульная структура

```
lib-math
  └── lib-minecraft
        └── lib-world
              └── lib-polar

engine-core                        (не зависит от lib-minecraft)
  ├── engine-ecs                   (engine-core + Fleks)
  ├── engine-minecraft             (engine-core + lib-minecraft; НЕ зависит от engine-ecs)
  ├── engine-bedrock               (engine-core + CloudburstMC Protocol)
  ├── engine-mcprotocollib         (engine-core + MCProtocolLib)
  └── engine-physics               (engine-core + Jolt JNI)
```

`engine-ecs` и `engine-minecraft` намеренно не зависят друг от друга.

### lib-math
`Vec3D`, `Vec3I`, `YawPitch`, `Cuboid` (AABB), `Transform`, `QRot`.

### lib-minecraft
Низкоуровневая реализация протокола Minecraft. Сеть: `Connection` (Netty), `NettyServer`, полный набор пакетов, шифрование, сжатие. Мир: `Block`, `ChunkSection`, `Palette`, `ChunkData`. Реестры: блоки, предметы, биомы, измерения, теги.

### lib-world
`ImmutableLayer`, `MutableLayer`, `LayeredWorld`, `ImmutableLayerKey`, `MutableLayerChangeTracker`.

### lib-polar
Загрузка миров из формата Polar (ZSTD): `loadAsLayer`, `loadAsMutableLayer`.

### engine-core
Протокол-независимое ядро. Не зависит от `lib-minecraft`. Ключевые абстракции: `Player`, `Instance`, `Tickable`, `PlayerInputProvider`, `PlayerOutputProvider`, `InstanceRouter`, `ConnectionRegistry`.

### engine-ecs
ECS без Minecraft. `EcsWorldTickable`, `PlayerIndex`, компоненты (`PlayerComponent`, `PositionComponent`, `ViewableComponent`, `LastSentPositionComponent`), базовые системы.

### engine-minecraft
Minecraft-реализация. `MinecraftPlayer`, `MinecraftConnectionService`, `MinecraftViewTickable`, `McEntityRegistry`, `ChunkPool`, `MinecraftWorldServiceHandler`.

### engine-bedrock
Bedrock-реализация. `BedrockPlayer`, `BedrockServer`, `BedrockSessionHandler`.

### engine-physics
`PhysicsSpace` (per-instance, Jolt), `TerrainGenerator` (per-instance, динамические terrain тела).

---

## Instance — единица изоляции

```kotlin
class Instance(
    val tickDuration: Duration,
    val tickables: List<Tickable>,
)
```

Каждый инстанс тикает независимо. Типичный состав `tickables`:
- `EcsWorldTickable` — ECS тик (если используется ECS)
- `MinecraftViewTickable` — отправка чанков и entity (per-instance)
- `MinecraftCommandTickable` — обработка команд (per-instance)

---

## Connection, ConnectionRegistry и Player

Три уровня:

**`Connection`** — глобальный, живёт пока клиент физически подключён. Только сетевой уровень: `sendPacket`, идентификатор. Не знает про игровую логику.

**`ConnectionRegistry`** (`@Singleton`) — глобальный маппинг:
- `connectionId → Connection`
- `connectionId → Player` (меняется при трансфере между инстансами)

**`Player`** (например `MinecraftPlayer`) — per-instance, создаётся при входе в инстанс, уничтожается при выходе. Хранит игровое состояние: `sentChunks`, `currentVisibleViewables` и т.д.

При трансфере: `Player` уничтожается в старом инстансе, создаётся новый в целевом. `Connection` при этом не меняется.

---

## Роутинг игроков

**`InstanceRouter`** (`@Singleton`) — отвечает за маппинг `connectionId → instanceId` и доставку UUID в нужный инстанс.

Два типа роутинга:

**Новый игрок** — определяется глобальной настройкой ноды через `PlayerRouter` интерфейс:
- Реализация указывает `defaultInstanceId` → игрок отправляется туда
- Или нода отвергает новых игроков (только переадресованные принимаются)

**Трансфер** — один инстанс говорит роутеру перекинуть игрока на другой:
```kotlin
instanceRouter.transferPlayer(uuid, targetInstanceId, transferData)
```

**`TransferData`** — `Map<String, Any>`. Передаётся при трансфере. `PlayerInitSystem` получает её вместе с UUID при создании entity.

---

## InstanceScope — per-instance DI

`InstanceScope` — per-instance DI контейнер. Создаётся в `InstanceFactory` при каждом вызове `create(prefab)`:

- `serverWorld: ServerWorld`
- `inputProvider: PlayerInputProvider` (composite из всех платформ инстанса)
- `outputProvider: PlayerOutputProvider` (composite)
- рендереры, каналы join/leave
- `physicsSpace`, `terrainGenerator` — lazy, создаются только если запрошены

---

## InstancePrefab — шаблон инстанса

```kotlin
data class InstancePrefab(
    val id: String,
    val platformIds: List<String>,       // ["minecraft", "bedrock"]
    val worlds: List<WorldLayerConfig>,
    val systems: List<EcsSystemConfig>,  // конфиги систем
)
```

Каждая система объявляет `Config : EcsSystemConfig` внутри себя и знает как создать себя из `InstanceScope`:
```kotlin
class PhysicsSystem(...) {
    object Config : EcsSystemConfig {
        override fun create(scope: InstanceScope) =
            PhysicsSystem(scope.physicsSpace, scope.terrainGenerator, ...)
    }
}
```

---

## ECS (если используется)

Реализация — Fleks.

**Принципы:**
- **Data-only компоненты.** `data class` без поведения, без ссылок на `File`/`Socket`/`Thread`.
- **Системы не хранят состояние в полях.** Всё состояние между тиками — в компонентах.
- **Явный порядок систем.** Никаких "приоритетов слушателей".

**Порядок систем в тике (демо):**
```
1. ReadClientPositionSystem    — позиция клиента → PositionComponent + LastSentPositionComponent
2. PlayerInitSystem            — join/leave из каналов → ECS entity
3. CommandActionsSystem        — выполнение команд из очереди
4. [Геймплейные системы]       — игровая логика
5. PhysicsSystem               — симуляция физики
6. ViewContextSyncSystem       — PlayerComponent.viewContextIDs → WorldService
7. WriteClientPositionSystem   — телепорт если PositionComponent != LastSentPositionComponent
8. ClearEventsSystem           — очистка event-компонентов
```

**`LastSentPositionComponent`** — отслеживает последнюю позицию отправленную клиенту. `WriteClientPositionSystem` шлёт телепорт только при расхождении с `PositionComponent`. `ReadClientPositionSystem` обновляет оба компонента одновременно — клиент уже знает свою позицию, повторно слать не нужно.

**`PlayerIndex`** — O(1) поиск ECS entity по UUID через `FamilyHook`.

---

## Renderer паттерн

Вместо View/Factory/Composite — один `Renderer` интерфейс на тип entity:

```kotlin
interface AxolotlRenderer {
    fun onAdd(id: UUID)
    fun onRemove(id: UUID)
    fun update(id, position, yawPitch, name, hiddenFromPlayer, viewContextIDs)
}
```

- `AxolotlModelSystem` — знает ECS, не знает платформу
- `MinecraftAxolotlRenderer` — знает Minecraft, не знает ECS, держит `HashMap<UUID, McEntity>`

Lifecycle через `FamilyHook` внутри системы: `onAdd`/`onRemove` при добавлении/удалении компонента.

---

## Система слоёв

`ServerWorld` хранит слои по `contextId`. Игрок видит только слои из своих `viewContextIDs`.

**Типы слоёв:**
- **Immutable** — загружается из Polar файла, кэшируется в `ChunkPool` (`@Singleton`, шарится между инстансами)
- **Mutable** — sparse, для динамического контента

**Семантика блоков:**
- `null` — слой не определяет блок, берётся из нижнего слоя
- `voidMarker` (`structure_void`) — явный воздух, вырезает нижние слои
- Любой другой блок — конкретный блок

**Алгоритм композиции** (сверху вниз по приоритету):
```
block = layer.getBlock(pos)
если block != null:
    если block == voidMarker → AIR
    иначе → block
иначе → следующий слой
```

**viewContextIDs** — что игрок видит. `ViewContextSyncSystem` синхронизирует в `WorldService`. Immutable композиция кэшируется в `ChunkPool`. Mutable overlay отправляется как `SectionBlocksUpdatePacket`.

**physContextIDs** — с чем тело сталкивается в физике. `TerrainGenerator` создаёт Jolt-тела только для активных контекстов. `ContactListener` фильтрует коллизии по пересечению контекстов.

---

## Физика

Jolt Physics через jolt-jni. Один `PhysicsSpace` на инстанс.

- Heartbeat паттерн: `beginTick` → `keepAlive` → `endTick` — тела без `keepAlive` удаляются автоматически
- `TerrainGenerator` — ключ кэша `TerrainKey(Vec3I, Set<String>)` — отдельные terrain тела для разных `physContextIDs`
- `MotionQuality` переключается динамически: `LinearCast` при скорости > порога, `Discrete` в остальных случаях
- Хитбокс игрока — Dynamic тело без гравитации. Тянется к позиции игрока через velocity. При расхождении позиций — клиенту отправляется velocity для плавного возврата.
