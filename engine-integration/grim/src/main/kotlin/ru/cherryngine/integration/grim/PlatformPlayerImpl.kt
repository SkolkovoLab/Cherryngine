package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.entity.GrimEntity
import ac.grim.grimac.platform.api.player.PlatformInventory
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.sender.Sender
import ac.grim.grimac.platform.api.world.PlatformWorld
import ac.grim.grimac.utils.math.Location
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.util.Vector3d
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*
import java.util.concurrent.CompletableFuture

class PlatformPlayerImpl(
    val player: Connection,
) : PlatformPlayer {
    override fun kickPlayer(textReason: String?) {
        player.kick(textReason.toString())
    }

    override fun isSneaking(): Boolean {
        return false
    }

    override fun setSneaking(b: Boolean) {

    }

    override fun hasPermission(s: String?): Boolean {
        return true
    }

    override fun hasPermission(s: String?, defaultIfUnset: Boolean): Boolean {
        return true
    }

    override fun sendMessage(message: String?) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: Component?) {
        TODO("Not yet implemented")
    }

    override fun updateInventory() {
        TODO("Not yet implemented")
    }

    override fun getPosition(): Vector3d? {
        TODO("Not yet implemented")
    }

    override fun getInventory(): PlatformInventory? {
        TODO("Not yet implemented")
    }

    override fun getVehicle(): GrimEntity? {
        TODO("Not yet implemented")
    }

    override fun getGameMode(): GameMode? {
        TODO("Not yet implemented")
    }

    override fun setGameMode(gameMode: GameMode?) {
        TODO("Not yet implemented")
    }

    override fun isExternalPlayer(): Boolean {
        TODO("Not yet implemented")
    }

    override fun sendPluginMessage(channelName: String?, byteArray: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun getSender(): Sender? {
        TODO("Not yet implemented")
    }

    override fun eject(): Boolean {
        TODO("Not yet implemented")
    }

    override fun teleportAsync(location: Location?): CompletableFuture<Boolean?>? {
        TODO("Not yet implemented")
    }

    override fun getNative(): Any {
        TODO("Not yet implemented")
    }

    override fun isDead(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getWorld(): PlatformWorld? {
        TODO("Not yet implemented")
    }

    override fun getLocation(): Location? {
        TODO("Not yet implemented")
    }

    override fun getUniqueId(): UUID? {
        TODO("Not yet implemented")
    }

    override fun isOnline(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }
}