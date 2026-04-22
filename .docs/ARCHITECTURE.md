# Cherryngine — Architecture

## Технический стек

- **Язык:** Kotlin 2.3 (jvmTarget = 25, JDK 25 toolchain)
- **DI:** Micronaut (compile-time, без рефлексии)
- **ECS:** Fleks (опционально)
- **Сеть Java Edition:** Netty + собственный pipeline; пакеты, NetworkBuffer и реестры — из [Minestom](https://github.com/Minestom/Minestom) (`net.minestom:minestom`)
- **Сеть Bedrock Edition:** CloudburstMC bedrock-connection поверх RakNet
- **Multiversion (Java):** ViaVersion + ViaBackwards
- **Anti-cheat (Java):** GrimAC + PacketEvents (опционально)
- **Физика:** Jolt Physics через jolt-jni
- **Команды:** Cloud command framework + Brigadier
- **Текст:** Kyori Adventure (MiniMessage)
- **Формат миров:** Polar (ZSTD)

## Использование Minestom

Из Minestom используются **только пакеты, реестры и data-классы** — это библиотека протокола 1.21.11 без серверной логики:

- `net.minestom.server.network.packet.client.* / server.*` — record-классы пакетов с `NetworkBuffer.Type<T>` сериализаторами
- `net.minestom.server.network.NetworkBuffer` — буфер для бинарной сериализации (мы оборачиваем Netty `ByteBuf` через адаптер)
- `net.minestom.server.network.packet.{PacketVanilla, PacketParser, PacketRegistry}` — id↔class реестры по `ConnectionState`
- `net.minestom.server.instance.{Block, Section, palette.Palette, heightmap.Heightmap}` — данные мира
- `net.minestom.server.world.{DimensionType, biome.Biome}`, `entity.{EntityType, Metadata, MetadataDef, ...}`, `item.{Material, ItemStack, ...}`, `world.attribute.*`, `message.ChatType`, `entity.damage.DamageType`, `dialog.Dialog`, `instance.fluid.Fluid`, `instance.block.{banner.BannerPattern, jukebox.JukeboxSong}`, `item.armor.{TrimMaterial, TrimPattern}`, `item.enchant.*`, `item.instrument.Instrument`, `entity.metadata.animal.*`
- `net.minestom.server.registry.{Registries, DynamicRegistry, RegistryKey}` — реестры
- `net.minestom.server.collision.ShapeImpl` — для извлечения AABB-списков коллизии блоков
- `net.minestom.server.MinecraftServer.updateProcess()` вызывается **один раз** при старте Micronaut'а (в `MinecraftModule.getRegistries()`). Это собирает дефолтные `DynamicRegistry`-и и заполняет глобальный `MinecraftServer.process()` — этот ServerProcess мы используем как Micronaut-бин типа `Registries`. **`start()` мы не вызываем** — никаких сокетов и тиков от Minestom не запускается. Сетевой стек, тики, инстансы, плеер-менеджмент — полностью наши.

То есть Minestom для нас — это «1.21.11 protocol & data SDK», а не сервер.

---

## Модульная структура

```
lib-math       lib-jackson
   └── engine-core ─── engine-ecs
           ├── engine-physics
           └── platform-minecraft-java ─── platform-minecraft-java/integration/{viaversion, grim}
                       └── platform-minecraft-bedrock
```

`engine-core`, `engine-ecs`, `engine-physics` не зависят ни от Minestom, ни от какой-либо платформы.

### engine-core
Платформенно-независимое ядро.
- `instance.{Instance, InstanceSingleton, InstanceSingletonScope, InstanceBeansFactory, ServerWorld, Tickable, TickStage, InstanceSetup}`
- `player.{Player, PlayerManager, InstanceRouter, PlayerRouter, PlayerPositionSource, PositionSnapshot, PlayerPositionShadow, PlayerPositionPreSyncTickable, PlayerPositionPostSyncTickable}`. `Player` имеет `clientPosition`/`clientYawPitch` (правда о положении клиента, пишет сеть платформы) и методы `teleport(...)`/`setVelocity(...)` (команды клиенту)
- `commandmanager.{CherryngineCommandManager (@InstanceSingleton), CommandSender, SArgumentParser, args/*}`
- `world.{TerrainCollisionProvider, WorldRaycaster, RaycastHit}` — кросс-платформенные контракты, реализуются `@Singleton`-ами в платформенных модулях с `canHandle(world)`
- `utils.{KyoriComponentExt, StableTicker}`, `Main`, `LoggerProvider`, `BeanCreationTimeLogger`

`ServerWorld` — маркерный интерфейс. Конкретные реализации (`MinecraftServerWorld`) живут в платформенных модулях.

### engine-ecs
Fleks-based ECS, опциональный.
- `EcsWorldTickable`, `EcsWorldBeanFactory`, `FleksTypes`, `Utils`
- Базовые компоненты: `PlayerComponent`, `PositionComponent`, `ViewableComponent`
- Базовые системы: `CommandActionsSystem`, `ClearEventsSystem`
- Events: `EcsEvent`
- `EcsPlayerPositionSource` — реализация `PlayerPositionSource` через `PositionComponent` (мост между ECS и платформенным sync позиции)

### engine-physics
Jolt Physics обёртка. Знает только `engine-core`.
- `PhysicsSpace` (per-instance)
- `terrain.{TerrainGenerator, ActiveBodyInfo}` — `TerrainGenerator` инжектит `List<TerrainCollisionProvider>` + `ServerWorld` и резолвит подходящего provider'а через `canHandle` лениво.

### platform-minecraft-java
Minecraft Java Edition. Содержит:
- **Сеть:** `network.{Connection, NettyServer, ConnectionHandler, ChannelHandlers, ChannelInjector, ByteBufVarInt}`, `network.protocol.{NetworkCompression, decoders/*, encoders/*, cryptography/*}`. `Connection` — `SimpleChannelInboundHandler<ClientPacket>`. Encoder/decoder ходят через `PacketVanilla.{CLIENT,SERVER}_PACKET_PARSER`, `NetworkBuffer.wrap/makeArray`, `PacketRegistry.PacketInfo`.
- **Player:** `player.{MinecraftPlayer, MinecraftConnectionService}`. `MinecraftPlayer` реализует `Player` — `clientPosition`/`clientYawPitch` обновляются в `MinecraftConnectionService.onMove()` при получении движения от клиента; `teleport()` шлёт `PlayerPositionAndLookPacket`; `setVelocity()` шлёт `EntityVelocityPacket` (с делением на 20)
- **Entity:** `entity.{McEntity, McEntityRegistry}`
- **World/мир:** `MinecraftServerWorld` (per-instance, реализует `ServerWorld`), `world.{Layer, ImmutableLayer, MutableLayer, LayeredWorld, LayerEntry, LayerClassification, LayeredWorld, MutableOverlay, World, VisibleBarriersWorld, ChunkPos, SectionPos, ChunkHeightmap, ChunkHeightmaps, MovePlayerFlags, MutableLayerChangeTracker, LightEngine, ImmutableLayerKey, MinecraftTerrainCollisionProvider, MinecraftWorldRaycaster}`, `world.chunk.ChunkData`, `world.utils.{BitStorage, SimpleBitStorage}`, `world.polar.{PolarReader, PolarWorldGenerator, PolarChunk, PolarSection, PolarWorld, PolarDataConverter, WorldHeightUtil}`
- **View:** `MinecraftViewTickable`, `view.{Viewable, BlocksViewable, ViewableProvider, StaticViewableProvider}`, `ChunkPool`
- **Команды:** `commandmanager.CommandNodeUtils`
- **DI/инфраструктура:** `MinecraftModule` (Micronaut Factory: `Registries` ← `MinecraftServer.updateProcess()`, `NettyServer`), `MinecraftInstanceBeansFactory` (per-instance bean для `MinecraftServerWorld`), `CherryngineRunner`, `EngineCoreConfig`, `ConnectionHandlerImpl`, `ServerConsts`
- **Events:** `events.{ConnectEvent, DisconnectEvent, PacketEvent, PlayerCreatedEvent, PlayerConfigurationAsyncEvent, SetGameProfileEvent}`
- **Utils:** `utils.{ChunkUtils, MojangUtil}`

### platform-minecraft-java/integration/viaversion
ViaVersion + ViaBackwards адаптер. Подключается опционально в impl-модуле.

### platform-minecraft-java/integration/grim
GrimAC anti-cheat + PacketEvents. Опциональный. Per-instance регистрация `/grim` команды через `GrimCommandBootstrap` (`@InstanceSingleton(eagerInit=true, platform="minecraft")`), глобальный `CommandManagerImpl` без зависимости на per-instance бины.

### platform-minecraft-bedrock
Bedrock Edition через CloudburstMC. Зависит от `platform-minecraft-java` (использует `MinecraftServerWorld` для общего реестра слоёв — Bedrock рендерит данные того же мира).
- `BedrockServer`, `BedrockSessionHandler`, `BedrockConfig`
- `BedrockPlayer` — реализует `Player`; `teleport()` шлёт `MovePlayerPacket` с `+1.62 Y` offset; `setVelocity()` шлёт `SetEntityMotionPacket` (с делением на 20)
- `entity.{BedrockEntity, BedrockEntityRegistry}`
- `world.{BedrockBlockMapping, BedrockChunkSerializer, BedrockViewTickable}`

---

## Instance — единица изоляции

```kotlin
class Instance(
    val tickDuration: Duration,
    val platformIds: Set<String>,
    private val appContext: ApplicationContext,
)
```

Каждый инстанс тикает независимо. `platformIds` определяет, какие платформенно-специфичные `@InstanceSingleton(platform = "...")`-бины подхватываются (фильтрация в `getAll()`/`startTicking()`/`initEager()` по platform-атрибуту).

Типичный состав tickables в minecraft-инстансе:
- `EcsWorldTickable` (если используется ECS)
- `MinecraftViewTickable` — отправка чанков и entity (per-instance)
- `MinecraftCommandTickable` — обработка очередей команд игроков
- `MinecraftPlayerPlatformTickable` — синхронизация хитбокс-платформы под игроком (демо)

---

## DI: глобальное vs per-instance

**Глобальное (`@Singleton`):** `Registries` (= `ServerProcess` Minestom), `NettyServer`, `PlayerManager`, `InstanceRouter`, `PlayerRouter`, `MinecraftConnectionService`, `ConnectionHandlerImpl`, `CherryngineRunner`, все `TerrainCollisionProvider` / `WorldRaycaster`, grim/viaversion-сервисы, рендер-фабрики `PlatformModule`-и.

**Per-instance (`@InstanceSingleton`):** разрешается только внутри активного `Instance` через `InstanceSingletonScope.withInstance(instance) { appContext.getBean(...) }` (ThreadLocal). Примеры: `CherryngineCommandManager`, `MinecraftServerWorld`, `McEntityRegistry`, `BedrockEntityRegistry`, `ChunkPool`, `PhysicsSpace`, `TerrainGenerator`, `MinecraftViewTickable`, `MinecraftCommandTickable`.

`InstanceBeansFactory.serverWorld(instance)` и `MinecraftInstanceBeansFactory.minecraftServerWorld(instance)` — мост из cache `Instance.register(Class, value)` в Micronaut DI: возвращают объект, лежащий в `Instance.cache`.

`Instance.initEager()` поднимает все `@InstanceSingleton(eagerInit = true)` бины при создании инстанса (с фильтром по платформе) — например `GrimCommandBootstrap` регистрирует `/grim` в `CherryngineCommandManager` нового инстанса.

---

## Connection и Player

Два уровня:

**`Connection`** (платформенно-специфичный, в `platform-minecraft-java/network/`) — глобальный, живёт пока клиент физически подключён. `SimpleChannelInboundHandler<ClientPacket>`. Только сетевой уровень: `sendPacket(ServerPacket)`, состояние `ConnectionState`, `gameProfile`, шифрование/сжатие. Не знает про игровую логику.

**`Player`** (`engine-core`, интерфейс) — per-instance. Конкретные реализации: `MinecraftPlayer`, `BedrockPlayer`. Создаётся при входе в инстанс, уничтожается при выходе. Хранит игровое состояние: `sentChunks`, `currentVisibleViewables`, `clientPosition` и т.д.

**`PlayerManager`** (`@Singleton`) — глобальный реестр UUID → Player. См. TODO про per-instance — сейчас один реестр на сервер.

При трансфере между инстансами `Player` уничтожается в старом инстансе и пересоздаётся в новом. `Connection` не меняется.

---

## Роутинг игроков

**`InstanceRouter`** (`@Singleton`) отвечает за маппинг `connectionId → instanceId` и передаёт UUID + `TransferData` в нужный инстанс.

**`PlayerRouter`** интерфейс для определения нового игрока: где должен оказаться вошедший впервые игрок. В демо — `DemoPlayerRouter` отправляет в `lobby`.

**`TransferData`** — `Map<String, Any>`, передаётся при трансфере. `PlayerInitSystem` получает её вместе с UUID при создании entity.

---

## InstanceFactory

В `impl-demo` — `InstanceFactory.create(prefab: InstancePrefab): Instance`. Поток:

1. Берёт `DimensionType.OVERWORLD` из `Registries`
2. Создаёт `MinecraftServerWorld`, регистрирует слои из `prefab.worlds` (грузит `.polar` из ресурсов через `PolarWorldGenerator.loadAsLayer/loadAsMutableLayer`, биомы резолвятся через `Registries.biome().getId(...)`)
3. Регистрирует `joinChannel`/`leaveChannel` в `InstanceRouter`
4. Создаёт `Instance`, кладёт в его cache: `InstancePrefab`, `ServerWorld` + `MinecraftServerWorld`, channels
5. `instance.initEager()` — поднимает eager-бины (например `GrimCommandBootstrap`)
6. Создаёт ECS-мир из `prefab.systems`
7. Регистрирует команды
8. `instance.startTicking()`

---

## InstancePrefab

```kotlin
data class InstancePrefab(
    val id: String,
    val platformIds: List<String>,    // ["minecraft", "bedrock"]
    val worlds: List<WorldLayerConfig>,
    val systems: List<EcsSystemConfig>,
)
```

Каждая ECS-система объявляет свой `Config : EcsSystemConfig` и знает, как создать себя из `Instance`:

```kotlin
class PhysicsSystem(...) {
    object Config : EcsSystemConfig {
        override fun create(instance: Instance) =
            PhysicsSystem(instance.get(), instance.get(), instance.get(), instance.get())
    }
}
```

---

## ECS

Реализация — Fleks.

**Принципы:**
- **Data-only компоненты.** `data class` без поведения.
- **Системы не хранят состояние в полях.** Всё состояние между тиками — в компонентах.
- **Явный порядок систем.** Никаких приоритетов слушателей.

**Порядок stages/tickables (демо):**
```
PRE:
  PlayerPositionPreSyncTickable  — клиент → PositionComponent (если источник его не трогал)
  PlayerHitboxPreSyncTickable    — driver.preSimulate: lifecycle хитбокса + velocity pull
GAME:
  EcsWorldTickable, прогоняющий ECS-системы в явном порядке:
    1. PlayerInitSystem            — join/leave из каналов → ECS entity
    2. CommandActionsSystem        — выполнение команд из очереди
    3. [Геймплейные системы]       — игровая логика
    4. CubePhysicsLifecycleSystem  — keepAlive + getOrCreateBody для кубов
    5. PhysicsSimulationSystem     — terrain.step + physicsSpace.update
    6. CubePhysicsSyncSystem       — body.transform → PositionComponent + CubeModelComponent
    7. HitboxVisualizationSystem   — hitbox position → HitboxVisualization entity
    8. ViewContextSyncSystem       — PlayerComponent.viewContextIDs → ServerWorld
    9. ClearEventsSystem           — очистка event-компонентов
POST:
  PlayerHitboxPostSyncTickable   — driver.postSimulate: meeting-point + player.setVelocity
  PlayerPositionPostSyncTickable — если PositionComponent != player.clientPosition → player.teleport(...)
  MinecraftViewTickable          — отправка чанков и видимостей
  MinecraftPlayerPlatformTickable — демо-синхронизация платформы под игроком
```

**Sync позиции** вынесен в `engine-core` и работает через `PlayerPositionSource` (аналогичен dispatcher'у рендереров): `PlayerPositionPreSyncTickable` и `PostSyncTickable` инжектят `List<PlayerPositionSource>`, делегируют по `canHandle(player)`. Shadow-state `PlayerPositionShadow` (`@InstanceSingleton`) хранит последнюю применённую позицию для каждого игрока; он **не сериализуется** — при restore ECS-компонентов из бэкапа shadow≠desired → POST автоматически отправит `teleport`.

**Серверный хитбокс игрока** вынесен из ECS в `impl-demo/hitbox/` через тот же паттерн: `PlayerHitboxDriver` (интерфейс в impl-demo, `canHandle + preSimulate + postSimulate`) + пара тикаблов-диспетчеров `PlayerHitboxPreSyncTickable` (PRE) и `PlayerHitboxPostSyncTickable` (POST). Единственная реализация — `DemoPlayerHitboxDriver` для `MinecraftPlayer`/`BedrockPlayer`: `preSimulate` отвечает за lifecycle хитбокса и velocity pull к `player.clientPosition`, `postSimulate` — за meeting-point и pushback через `player.setVelocity`. Гипотетическая платформа с нативными клиентскими коллизиями не попадает под `canHandle` → хитбокс не создаётся, pushback не отправляется. ECS занимается только lifecycle/sync физических тел-кубов.

`PlayerIndex` — O(1) поиск ECS entity по UUID через Fleks `FamilyHook`.

---

## Renderer паттерн

Один `Renderer` интерфейс на тип entity, в `impl-demo/src/.../renderer/`:

```kotlin
interface AxolotlRenderer {
    fun onAdd(id: UUID)
    fun onRemove(id: UUID)
    fun update(id, position, yawPitch, name, hiddenFromPlayer, viewContextIDs)
}
```

- `AxolotlModelSystem` — знает ECS, не знает платформу
- `MinecraftAxolotlRenderer` (`@InstanceSingleton(platform = "minecraft")`) — знает Minecraft, не знает ECS, держит `HashMap<UUID, McEntity>`
- `BedrockAxolotlRenderer` (`@InstanceSingleton(platform = "bedrock")`) — то же для bedrock

Lifecycle через `FamilyHook` внутри системы: `onAdd`/`onRemove` при добавлении/удалении компонента.

---

## Layer System

`MinecraftServerWorld` хранит слои по `contextId`. Игрок видит только слои из своих `viewContextIDs`.

**Типы слоёв:**
- **`ImmutableLayer`** — загружается из Polar файла, кэшируется в `ChunkPool` (`@InstanceSingleton`, шарится между инстансами через `ImmutableLayerKey`)
- **`MutableLayer`** — sparse, для динамического контента, поддерживает `MutableLayerChangeTracker`

**Семантика блоков:**
- `null` от `Layer.getBlock(pos)` — слой не определяет блок, берётся из нижнего слоя
- `voidMarker` (`structure_void` по умолчанию) — явный воздух, вырезает нижние слои
- Любой другой блок — конкретный блок

**Алгоритм композиции** (сверху вниз по приоритету):
```
block = layer.getBlock(pos)
если block != null:
    если block == voidMarker → AIR
    иначе → block
иначе → следующий слой
```

`viewContextIDs` — что игрок видит. `ViewContextSyncSystem` синхронизирует в `MinecraftServerWorld`. Immutable композиция кэшируется в `ChunkPool`. Mutable overlay отправляется как `MultiBlockChangePacket` через `MutableOverlay.computeOverlay()`.

`physContextIDs` — с чем тело сталкивается в физике. `TerrainGenerator` создаёт Jolt-тела только для активных контекстов. `PhysicsSpace.ContactListener` фильтрует коллизии по пересечению контекстов.

---

## Физика

Jolt Physics через jolt-jni. Один `PhysicsSpace` на инстанс.

- **Heartbeat:** `keepAlive(id)` маркирует тело живым в текущем тике, `update(delta)` инкапсулирует симуляцию + unseen-cleanup: тела без `keepAlive` этого тика удаляются в конце `update`. Никаких явных `beginTick/endTick` в API.
- **`collectActiveBodies(): List<ActiveBodyInfo>`** — снимок всех живых тел с их `physContextIDs`. Используется `PhysicsSimulationSystem` для передачи в `TerrainGenerator.step()`.
- **Provider-based коллизии:** `TerrainGenerator` инжектит `List<TerrainCollisionProvider>`, при первом обращении выбирает того, у кого `canHandle(serverWorld) == true`. Это позволяет engine-physics не знать ни про Minecraft, ни про Bedrock. `MinecraftTerrainCollisionProvider` берёт реальные AABB-кубойды из `Block.registry().collisionShape()` (cast в `ShapeImpl.boundingBoxes()`) — точные коллизии лестниц/плит/заборов.
- **`MotionQuality`** переключается динамически: `LinearCast` при скорости > порога, `Discrete` в остальных случаях.
- **Хитбокс игрока** — Dynamic тело без гравитации. Создаётся и ведётся `DemoPlayerHitboxDriver` (`impl-demo/hitbox/`), не через ECS-компонент. `preSimulate` в PRE-stage делает velocity pull к `player.clientPosition`; `postSimulate` в POST-stage — meeting-point и `player.setVelocity` (pushback).
- **Лайфцикл кубов** в ECS: `CubePhysicsLifecycleSystem` (keepAlive + getOrCreateBody) → `PhysicsSimulationSystem` (terrain.step + physicsSpace.update) → `CubePhysicsSyncSystem` (transform body → PositionComponent + CubeModelComponent).

`WorldRaycaster` — аналогичный platform-agnostic контракт для raycast по блокам, возвращает `RaycastHit(hitPos, blockPos, blockMaterial: String)`.
