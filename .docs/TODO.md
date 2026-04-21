# Cherryngine — TODO

## Архитектура

- [ ] **`ConnectionRegistry`** — реализовать глобальный реестр `connectionId → Connection` и `connectionId → Player`. Сейчас `MinecraftPlayer` живёт в глобальном `PlayerManager`, что противоречит философии per-instance Player.

- [ ] **`PlayerManager` per-instance** — после реализации `ConnectionRegistry` перевести `PlayerManager` в per-instance (через `@InstanceSingleton`). `onlinePlayers()` должен возвращать только игроков текущего инстанса.

- [ ] **`PlayerService` и `WorldService`** — переосмыслить. Сейчас написаны наспех. `JoinGamePacket` содержит данные от игровой логики — нужен чистый механизм для этого.

- [ ] **YAML конфиг для префабов** — сейчас префаб захардкожен в `DemoInit`. Реализовать загрузку из YAML.

## Физика

- [ ] **`PhysicsSpace.destroy()`** — не реализован, TODO в коде.

## Отрисовка / протокол

- [ ] **`KnownPacks` exchange** — `MinecraftConnectionService` шлёт полные `RegistryDataPacket`-и (`excludeVanilla=false`). Реализовать handshake с `SelectKnownPacksPacket` для оптимизации трафика конфигурейшн-фазы.

- [ ] **Custom Metadata API** — `McEntity.metadata` сейчас сырой `MutableMap<Int, Metadata.Entry<*>>`. Завернуть в типизированный wrapper над Minestom `MetadataHolder`/`MetadataDef`.

## ECS

- [ ] **Fleks → последняя версия** — следить за обновлениями, мигрировать при появлении нужных фич.

## Открытые вопросы

- [ ] **Освещение в композированных чанках** — как считать свет для микса слоёв.

- [ ] **Smart resubscribe** — минимизация пакетов при смене `viewContextIDs` у игрока.

- [ ] **Тики с разной частотой** — нужны ли инстансам разные TPS?

- [ ] **ChunkPool дублирование** — мир и мир-с-квартирой дублируют все чанки, хотя отличаются только парой. Нужна дельта-кэширование по базовому слою.

- [ ] **Глобальные сервисы** — профили, статистика, экономика, чат между инстансами — когда понадобится.

## Геймплей (в демо, потенциально в движок)

- [ ] **`ApartSystem`** — доработать и перенести в движок как универсальный механизм инстанс-порталов.
