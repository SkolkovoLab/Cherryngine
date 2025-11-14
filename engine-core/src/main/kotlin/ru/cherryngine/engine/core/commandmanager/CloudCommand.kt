package ru.cherryngine.engine.core.commandmanager

import jakarta.inject.Singleton

@Singleton
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CloudCommand()
