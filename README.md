# Cherryngine

### [English README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README_EN.md)

Kotlin-фреймворк для Minecraft-серверов с платформонезависимой архитектурой. Поддерживает Java Edition и Bedrock Edition; теоретически можно реализовать поддержку любой другой платформы — от Hytale до Team Fortress.

## Ключевые идеи

**Кроссплатформенность** — любой функционал взаимодействия с клиентом оформляется через интерфейс. "Отрисовать модель", "переместить игрока", "показать HP" — для каждой платформы своя реализация, геймплей дёргает только контракт.

**Инстанс** — единица изоляции. Лобби, матч, арена, квартира — каждый инстанс полностью изолирован: своя физика, свой ECS-мир, свои игроки. Несколько инстансов в одном JVM-процессе шарят тяжёлые объекты (кэши чанков и т.д.).

**ECS** (который Entity Component System, а не [EliteClubSessions](https://www.youtube.com/@eliteclubsessions)) — рекомендуемый способ писать геймплей, но не обязательный. Хочешь — используй Fleks, не хочешь — любой класс реализующий `Tickable` становится частью игрового цикла.

**Per-instance DI** — бины помеченные `@InstanceSingleton` создаются один раз на инстанс, lazy, и уничтожаются вместе с ним. Платформенные бины фильтруются по `platform` — в инстанс попадает только то, что нужно:

```kotlin
@InstanceSingleton(platform = "minecraft")
class MinecraftAxolotlRenderer : AxolotlRenderer
// создаётся только если инстанс включает платформу "minecraft"
```

## Структура модулей

```
engine-core      — платформенно-независимое ядро
engine-ecs       — опциональный ECS (Fleks)
engine-physics   — физика (Jolt), не зависит от платформ
platform-minecraft-java    — Minecraft Java Edition
platform-minecraft-bedrock — Minecraft Bedrock Edition
```

## Демо проект

https://github.com/SkolkovoLab/CherryngineDemo

---

## Благодарочки

- [Minestom](https://github.com/Minestom/Minestom) — за то что положили конец монополии Bukkit и открыли глаза на то, что крупный сервер в Minecraft можно сделать без NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — за кодовую базу модуля `lib-minecraft`
- [Rayon](https://github.com/lazurite-dev/rayon) — за референс по физике блоков
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — за детство, легенда
- Марсик — за то, что не наблевал на кровать сегодня 🥰