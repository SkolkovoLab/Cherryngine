package ru.cherryngine.lib.minecraft.world

/**
 * Упрощённое представление флагов движения игрока. Используется как удобный
 * типизированный контейнер при обмене между compact-сервисами.
 */
data class MovePlayerFlags(
    val isOnGround: Boolean,
    val horizontalCollision: Boolean,
)
