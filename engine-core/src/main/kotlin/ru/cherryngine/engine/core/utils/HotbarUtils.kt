package ru.cherryngine.engine.core.utils

/**
 * Дельта переключения хотбар-слота с учётом wrap 8↔0.
 * Возвращает знаковое число шагов колеса (диапазон −4..+4).
 */
fun scrollAmount(prevSlot: Int, newSlot: Int): Int {
    var addSlot = newSlot - prevSlot
    if (addSlot > 4) addSlot -= 9 else if (addSlot < -4) addSlot += 9
    return addSlot
}
