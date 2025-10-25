package io.github.dockyardmc.tide.types

sealed interface Either<L, R> {
    val value: Any?

    data class Left<L, R>(
        override val value: L
    ) : Either<L, R>

    data class Right<L, R>(
        override val value: R
    ) : Either<L, R>
}