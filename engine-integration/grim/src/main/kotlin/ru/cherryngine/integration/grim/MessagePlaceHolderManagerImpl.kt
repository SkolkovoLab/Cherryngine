package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.manager.MessagePlaceHolderManager
import ac.grim.grimac.platform.api.player.PlatformPlayer

class MessagePlaceHolderManagerImpl : MessagePlaceHolderManager {
    override fun replacePlaceholders(player: PlatformPlayer?, string: String): String = string
}