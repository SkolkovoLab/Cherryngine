package ru.cherryngine.integration.grim.manager

import ac.grim.grimac.platform.api.manager.ItemResetHandler
import ac.grim.grimac.platform.api.player.PlatformPlayer
import com.github.retrooper.packetevents.protocol.player.InteractionHand
import jakarta.inject.Singleton

@Singleton
class ItemResetHandlerImpl : ItemResetHandler {
    override fun resetItemUsage(player: PlatformPlayer?) {
        TODO("Not yet implemented")
    }

    override fun getItemUsageHand(player: PlatformPlayer?): InteractionHand {
        TODO("Not yet implemented")
    }
}