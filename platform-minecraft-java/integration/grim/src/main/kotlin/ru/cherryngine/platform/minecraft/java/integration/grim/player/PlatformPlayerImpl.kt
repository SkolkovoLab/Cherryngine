package ru.cherryngine.platform.minecraft.java.integration.grim.player

import ac.grim.grimac.platform.api.entity.GrimEntity
import ac.grim.grimac.platform.api.player.PlatformInventory
import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.sender.Sender
import ac.grim.grimac.platform.api.sender.SenderFactory
import ac.grim.grimac.platform.api.world.PlatformWorld
import ac.grim.grimac.utils.math.Location
import com.github.retrooper.packetevents.protocol.player.GameMode
import com.github.retrooper.packetevents.util.Vector3d
import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.java.player.MinecraftPlayer
import java.util.*
import java.util.concurrent.CompletableFuture

class PlatformPlayerImpl(
    private val player: MinecraftPlayer,
    private val senderFactory: SenderFactory<CommandSender>,
) : PlatformPlayer {
    private val world = PlatformWorldImpl(player)

    override fun kickPlayer(textReason: String?) {
        player.connection.kick(textReason.toString())
    }

    override fun isSneaking(): Boolean = player.isSneaking

    override fun setSneaking(b: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(s: String?): Boolean = false

    override fun hasPermission(s: String?, defaultIfUnset: Boolean): Boolean = false

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
        val pos = player.clientPosition
        return Vector3d(pos.x, pos.y, pos.z)
    }

    override fun getInventory(): PlatformInventory = PlatformInventoryImpl()

    override fun getVehicle(): GrimEntity {
        TODO("Not yet implemented")
    }

    override fun getGameMode(): GameMode {
        TODO("Not yet implemented")
    }

    override fun setGameMode(gameMode: GameMode) {
        TODO("Not yet implemented")
    }

    override fun isExternalPlayer(): Boolean = false

    override fun sendPluginMessage(channelName: String?, byteArray: ByteArray?) {
        TODO("Not yet implemented")
    }

    override fun getSender(): Sender = senderFactory.wrap(player)

    override fun eject(): Boolean {
        TODO("Not yet implemented")
    }

    override fun teleportAsync(location: Location): CompletableFuture<Boolean> {
        val vec3D = Vec3D(location.x, location.y, location.z)
        val yawPitch = YawPitch(location.yaw, location.pitch)
        player.teleport(vec3D, yawPitch)
        return CompletableFuture.completedFuture(true)
    }

    override fun getNative(): Any = player

    override fun isDead(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getWorld(): PlatformWorld = world

    override fun getLocation(): Location {
        val pos = player.clientPosition
        val yp = player.clientYawPitch
        return Location(world, pos.x, pos.y, pos.z, yp.yaw, yp.pitch)
    }

    override fun distanceSquared(x: Double, y: Double, z: Double): Double =
        player.clientPosition.minus(x, y, z).lengthSquared()

    override fun getUniqueId(): UUID = player.uuid

    override fun isOnline(): Boolean = player.connection.isActive

    override fun getName(): String = player.username
}
