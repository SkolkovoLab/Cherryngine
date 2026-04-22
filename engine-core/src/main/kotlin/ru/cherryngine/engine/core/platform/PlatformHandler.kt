package ru.cherryngine.engine.core.platform

interface PlatformHandler<in T> {
    fun canHandle(target: T): Boolean
}