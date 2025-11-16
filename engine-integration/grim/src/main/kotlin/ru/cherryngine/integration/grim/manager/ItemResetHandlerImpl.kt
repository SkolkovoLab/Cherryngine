package ru.cherryngine.integration.grim.manager

import ac.grim.grimac.platform.api.manager.ItemResetHandler
import ac.grim.grimac.platform.api.player.PlatformPlayer
import com.github.retrooper.packetevents.protocol.player.InteractionHand
import jakarta.inject.Singleton

@Singleton
class ItemResetHandlerImpl : ItemResetHandler {
    override fun resetItemUsage(player: PlatformPlayer?) {

    }

    override fun getItemUsageHand(player: PlatformPlayer?): InteractionHand {
        return InteractionHand.MAIN_HAND
    }

    override fun isUsingItem(player: PlatformPlayer?): Boolean {
        return false
    }
}