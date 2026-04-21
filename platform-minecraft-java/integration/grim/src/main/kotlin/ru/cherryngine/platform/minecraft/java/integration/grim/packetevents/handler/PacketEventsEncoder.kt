package ru.cherryngine.platform.minecraft.java.integration.grim.packetevents.handler

import ac.grim.grimac.utils.viaversion.ViaVersionUtil
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.event.ProtocolPacketEvent
import com.github.retrooper.packetevents.exception.CancelPacketException
import com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend
import com.github.retrooper.packetevents.exception.PacketProcessException
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper
import com.github.retrooper.packetevents.protocol.ConnectionState
import com.github.retrooper.packetevents.protocol.PacketSide
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.ExceptionUtil
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import net.kyori.adventure.text.Component
import ru.cherryngine.platform.minecraft.java.network.Connection

@ChannelHandler.Sharable
class PacketEventsEncoder(
    private val side: PacketSide,
    var user: User,
    private val preViaVersion: Boolean,
) : ChannelOutboundHandlerAdapter() {
    var player: Connection? = null
    private var promise: ChannelPromise? = null

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg !is ByteBuf) {
            ctx.write(msg, promise)
            return
        }

        // Handle promise management first (matches Spigot)
        val oldPromise = if (this.promise != null && this.promise?.isSuccess == false) {
            this.promise
        } else {
            null
        }

        var currentPromise = promise

        // "unvoid" will just make sure we can actually add listeners to this promise...
        // since 1.21.6, mojang will give us void promises as they don't care about the result
        currentPromise = currentPromise.unvoid()

        currentPromise.addListener { _ -> this.promise = oldPromise }
        this.promise = currentPromise

        // Process the packet and execute post-send tasks (matches Spigot)
        handlePacket(ctx, msg, currentPromise)

        // Check for empty packets last (matches Spigot)
        if (!ByteBufHelper.isReadable(msg)) {
            throw CancelPacketException.INSTANCE
        } else {
            ctx.write(msg, currentPromise)
        }
    }

    @Throws(Exception::class)
    private fun handlePacket(
        ctx: ChannelHandlerContext,
        buffer: ByteBuf,
        promise: ChannelPromise,
    ): ProtocolPacketEvent? {
        val u = user

        if (!preViaVersion && PacketEvents.getAPI().settings.isPreViaInjection && !ViaVersionUtil.isAvailable) {
            PacketEventsImplHelper.handlePacket(
                ctx.channel(), u, player, buffer, preViaVersion, side
            )
        }

        val protocolPacketEvent = PacketEventsImplHelper.handlePacket(
            ctx.channel(), u, player, buffer, !preViaVersion, side
        )

        if (protocolPacketEvent is PacketSendEvent && protocolPacketEvent.hasTasksAfterSend()) {
            promise.addListener { _ ->
                for (task in protocolPacketEvent.tasksAfterSend) {
                    task.run()
                }
            }
        }

        return protocolPacketEvent
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Handle CancelPacketException (similar to Spigot)
        if (ExceptionUtil.isException(cause, CancelPacketException::class.java)) {
            return
        }

        // Handle InvalidDisconnectPacketSend (similar to Spigot)
        if (ExceptionUtil.isException(cause, InvalidDisconnectPacketSend::class.java)) {
            return
        }

        // Handle PacketProcessException (similar to Spigot)
        val didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException::class.java)
        if (didWeCauseThis && (user.encoderState != ConnectionState.HANDSHAKING)) {
            if (PacketEvents.getAPI().settings.isFullStackTraceEnabled) {
                cause.printStackTrace()
            } else {
                PacketEvents.getAPI().logManager.warn(cause.message)
            }

            if (PacketEvents.getAPI().settings.isKickOnPacketExceptionEnabled) {
                try {
                    val disconnectPacket = WrapperPlayServerDisconnect(Component.text("Invalid packet"))
                    user.sendPacket(disconnectPacket)
                } catch (_: Exception) {
                    // Ignore exceptions during disconnect (similar to Spigot)
                }

                ctx.channel().close()

                PacketEvents.getAPI().logManager.warn(
                    "Disconnected ${user.profile.name} due to invalid packet!"
                )
            }
        }

        super.exceptionCaught(ctx, cause)
    }
}
