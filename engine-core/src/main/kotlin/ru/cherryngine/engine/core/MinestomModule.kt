package ru.cherryngine.engine.core

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import net.minestom.server.gamedata.tags.TagManager

@Factory
class MinestomModule {
    @Singleton
    fun getTagManager() = TagManager()
}