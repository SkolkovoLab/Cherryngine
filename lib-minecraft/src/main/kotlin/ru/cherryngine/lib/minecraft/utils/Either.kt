package ru.cherryngine.lib.minecraft.utils

sealed interface Either<L, R> {
    val value: Any?

    data class Left<L, R>(
        override val value: L,
    ) : Either<L, R>

    data class Right<L, R>(
        override val value: R,
    ) : Either<L, R>

    fun <T> unify(
        leftMapper: (L) -> T,
        rightMapper: (R) -> T,
    ): T = when (this) {
        is Left -> leftMapper(value)
        is Right -> rightMapper(value)
    }
}