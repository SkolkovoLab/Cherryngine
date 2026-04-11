# Cherryngine

### [English README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README_EN.md)

Ядро для Minecraft серверов на Kotlin, которое мы пишем с нуля.

Вы видели CounterMine? Если нет — посмотрите: https://cherry.pizza/
<br>
Первое ядро для него было на [Minestom](https://github.com/Minestom/Minestom), и мы столкнулись с рядом проблем:

- Нет многопоточности — лаги в одном мире роняют TPS везде
- Негибкий ViewSystem — работает только с Entity, а нам нужны составные модели
- Нет Netty — нельзя подключить ViaVersion

Плюс проект так разросся, что добавление новых фич превратилось в кошмар.

Так и появился Cherryngine — новое ядро с нуля, без Minestom.

---

## Ключевые идеи

**Кроссплатформенность.** Архитектура протокол-независимая: в одном инстансе могут одновременно играть игроки с разных платформ и взаимодействовать друг с другом. Добавить новую платформу — написать один `@Singleton`-хендлер, всё остальное не трогать.

**Платформа первична, ECS — адаптер.** ECS системы не знают про Minecraft. Платформенная логика живёт отдельно и не лезет в игровую.

**Layer System.** Каждый игрок видит свой вид мира — без копирования мира. Квартиры в GTA-стиле, персональные билд-баттл-холсты, физика по персональным блокам — всё это из коробки.

---

## Что внутри

**`lib-minecraft`** — низкоуровневая реализация протокола Minecraft поверх Netty. Базирован на [Dockyard](https://github.com/DockyardMC/Dockyard), но существенно переработан. Если нужна котлиновая либа для сырого взаимодействия с протоколом — вот она.

**`engine-core`** — протокол-независимое ядро. `Instance`, `Tickable`, `Player` (интерфейс), сервисы с кроссплатформенной диспетчеризацией.

**`engine-minecraft`** — Minecraft-реализация поверх `engine-core`. Управление соединениями, отправка чанков, McEntity, команды. Не зависит от ECS.

**`engine-ecs`** — ECS (который Entity Component System, а не [EliteClubSessions](https://www.youtube.com/@eliteclubsessions)) на базе [Fleks](https://github.com/Quillraven/Fleks). Вся игровая логика живёт здесь. Не зависит от Minecraft.

**`engine-physics`** — физика на базе [Jolt Physics](https://github.com/jrouwe/JoltPhysics) через JNI. Terrain generation по образцу [Rayon](https://github.com/lazurite-dev/rayon) — блоки добавляются в физический мир динамически вокруг активных тел.

**`lib-world`** — Layer System. Самая нетривиальная часть ядра.

---

## Layer System

Нативная поддержка персонального вида мира для каждого игрока — без копирования мира.

- **Квартиры в GTA-стиле** — у каждого своя квартира на одних координатах, снаружи не видна
- **Билд-баттл** — у каждого свой холст, во время голосования все видят один
- **Физика** — выброшенный предмет сталкивается с блоками твоей квартиры, а не чужой

Работает через `viewContextIDs` (что видит игрок) и `physContextIDs` (с чем сталкивается физически).

---

## Мультиверсия

ViaVersion + ViaBackwards через `engine-integration:viaversion`. Клиенты могут заходить со старых версий.

---

## Демо

https://github.com/SkolkovoLab/CherryngineDemo

---

## Благодарочки

- [Minestom](https://github.com/Minestom/Minestom) — за то что положили конец монополии Bukkit и открыли глаза на то, что крупный сервер в Minecraft можно сделать без NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — за кодовую базу модуля `lib-minecraft`
- [Rayon](https://github.com/lazurite-dev/rayon) — за референс по физике блоков
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — за детство, легенда
- Марсик — за то, что не наблевал на кровать сегодня 🥰