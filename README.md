# Cherryngine

### [English README](https://github.com/SkolkovoLab/Cherryngine/blob/master/README_EN.md)

Крч в будущем это мега инновационное ядро для создания серваков

Ну вы видели CounterMine, а если не видели, то посмотрите https://cherry.pizza/

Первая версия ядра, та которая на CounterMine основана на [Minestom](https://github.com/Minestom/Minestom), но мы столкнулись с некоторыми проблемами:
- Отсутствие многопоточности — если в одном из миров падает ТПС, лагать начинают все миры
- Майнстом не гибкий — ViewSystem хавает исключительно Entity, а мы юзаем составные модельки
- Отсутствие Netty — убивает возможность подключить ViaVersion

Помимо проблем майнстома у нас ещё и свои заёбы имеются, проект настолько разжирел, что добавление новых фич превращается в сущий кошмар

И я затеял разработку нового ядра (вот этого вот), с нуля, без Minestom

---

## Что внутри

**`lib-minecraft`** — низкоуровневая реализация протокола Minecraft 1.21.11 поверх Netty. Базирован на [Dockyard](https://github.com/DockyardMC/Dockyard), но переписан. Если нужна котлиновая либа для сырого взаимодействия с протоколом — вот она.

**`engine-core`** — набор инструментов поверх `lib-minecraft`. DI через Micronaut. Управление соединениями, отправка чанков, Layer System.

**`engine-ecs`** — ECS на базе [Fleks](https://github.com/Quillraven/Fleks). Вся игровая логика живёт здесь.

**`engine-physics`** — физика на базе [Jolt Physics](https://github.com/jrouwe/JoltPhysics) через JNI. Terrain generation по образцу [Rayon](https://github.com/lazurite-dev/rayon) — блоки добавляются в физический мир динамически вокруг активных тел.

**`lib-world`** — Layer System. Самая нетривиальная часть ядра.

---

## Layer System

Нативная поддержка персонального вида мира для каждого игрока — без копирования мира.

Примеры:
- **Квартиры в GTA-стиле** — у каждого игрока своя квартира на одних координатах. Снаружи квартира не видна, внутри — только твоя.
- **Билд-баттл** — у каждого свой холст, во время голосования все смотрят один холст.
- **Физика** — выброшенный предмет сталкивается с блоками твоей квартиры, а не чужой.

Работает через `viewContextIDs` (что видит игрок) и `physContextIDs` (с чем сталкивается физически).

---

## Мультиверсия

ViaVersion + ViaBackwards через `engine-integration:viaversion`. Сервер работает на протоколе 1.21.11, клиенты могут заходить со старых версий.

---

## Демо

https://github.com/SkolkovoLab/CherryngineDemo

---

## Благодарочки
- [Minestom](https://github.com/Minestom/Minestom) — за то что наконец положили конец монополии баккита и открыли глаза на то, что крупный сервак в майне можно сделать без NMS
- [Dockyard](https://github.com/DockyardMC/Dockyard) — за код на котором базирован модуль `lib-minecraft`
- [Rayon](https://github.com/lazurite-dev/rayon) — за референс по физике блоков
- [EliteClubSessions](https://www.youtube.com/@eliteclubsessions) — за детство, легенда
- Марсик — за то, что не наблевал на кровать сегодня 🥰