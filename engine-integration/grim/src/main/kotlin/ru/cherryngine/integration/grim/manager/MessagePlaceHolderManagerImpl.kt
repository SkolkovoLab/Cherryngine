package ru.cherryngine.integration.grim.manager

import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager
import ac.grim.grimac.platform.api.player.PlatformPlayer
import jakarta.inject.Singleton

@Singleton
class MessagePlaceHolderManagerImpl : MessagePlaceHolderManager {
    override fun replacePlaceholders(player: PlatformPlayer?, string: String): String = string
}