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
import ru.cherryngine.engine.core.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*
import java.util.concurrent.CompletableFuture

class PlatformPlayerImpl(
    val player: Player,
) : PlatformPlayer, Sender {
    override fun kickPlayer(textReason: String?) {
        player.connection.kick(textReason.toString())
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

    override fun performCommand(commandLine: String?) {
        TODO("Not yet implemented")
    }

    override fun isConsole(): Boolean {
        return false
    }

    override fun isPlayer(): Boolean {
        return true
    }

    override fun getNativeSender(): Any {
        return player
    }

    override fun getPlatformPlayer(): PlatformPlayer {
        return this
    }

    override fun sendMessage(message: String) {
        player.sendMessage(message)
    }

    override fun sendMessage(message: Component) {
        player.sendMessage(message)
    }

    override fun updateInventory() {
        TODO("Not yet implemented")
    }

    override fun getPosition(): Vector3d {
        val (x, y, z) = player.clientPosition
        return Vector3d(x, y, z)
    }

    override fun getInventory(): PlatformInventory {
        TODO("Not yet implemented")
    }

    override fun getVehicle(): GrimEntity {
        TODO("Not yet implemented")
    }

    override fun getGameMode(): GameMode {
        TODO("Not yet implemented")
    }

    override fun setGameMode(gameMode: GameMode) {
        TODO("Not yet implemented")
    }

    override fun isExternalPlayer(): Boolean {
        TODO("Not yet implemented")
    }

    override fun sendPluginMessage(channelName: String?, byteArray: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun getSender(): Sender {
        return this
    }

    override fun eject(): Boolean {
        TODO("Not yet implemented")
    }

    override fun teleportAsync(location: Location): CompletableFuture<Boolean> {
        val vec3D = Vec3D(location.x, location.y, location.z)
        val yawPitch = YawPitch(location.yaw, location.pitch)
        player.teleport(vec3D, yawPitch)
        return CompletableFuture.completedFuture(true)
    }

    override fun getNative(): Any {
        TODO("Not yet implemented")
    }

    override fun isDead(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getWorld(): PlatformWorld {
        TODO("Not yet implemented")
    }

    override fun getLocation(): Location {
        val (x, y, z) = player.clientPosition
        val (yaw, pitch) = player.clientYawPitch
        return Location(null, x, y, z, yaw, pitch)
    }

    override fun getUniqueId(): UUID {
        return player.uuid
    }

    override fun isOnline(): Boolean {
        return player.connection.isActive
    }

    override fun getName(): String {
        return player.username
    }
}