package ru.cherryngine.integration.grim.packetevents.handler

import ac.grim.grimac.utils.viaversion.ViaVersionUtil
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.event.ProtocolPacketEvent
import com.github.retrooper.packetevents.exception.CancelPacketException
import com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend
import com.github.retrooper.packetevents.exception.PacketProcessException
import com.github.retrooper.packetevents.manager.server.ServerVersion
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper
import com.github.retrooper.packetevents.protocol.ConnectionState
import com.github.retrooper.packetevents.protocol.PacketSide
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.EventCreationUtil
import com.github.retrooper.packetevents.util.ExceptionUtil
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.util.ReferenceCountUtil
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.server.Connection

@ChannelHandler.Sharable
class PacketEventsEncoder(
    private val side: PacketSide,
    var user: User,
    private val preViaVersion: Boolean,
) : ChannelOutboundHandlerAdapter() {

    companion object {
        // ancient Minecraft versions don't support this api
        // https://howoldisminecraft188.today/
        private val NETTY_4_1_0: Boolean = try {
            ChannelPromise::class.java.getDeclaredMethod("unvoid")
            true
        } catch (ignored: NoSuchMethodException) {
            false
        }
    }

    var player: Connection? = null
    private var promise: ChannelPromise? = null
    private var handledCompression: Boolean = false
    private val isPre1_20_5: Boolean = PacketEvents.getAPI().serverManager.version.isOlderThan(ServerVersion.V_1_20_5)

    fun read(originalCtx: ChannelHandlerContext, buffer: ByteBuf, promise: ChannelPromise) {
//        val ctx = tryFixCompressorOrder(originalCtx, buffer)
        val ctx = originalCtx
        val firstReaderIndex = buffer.readerIndex()
        val packetSendEvent = EventCreationUtil.createSendEvent(
            ctx.channel(), user, player, buffer, true
        )
        val readerIndex = buffer.readerIndex()
        PacketEvents.getAPI().eventManager.callEvent(packetSendEvent) {
            buffer.readerIndex(readerIndex)
        }

        if (!packetSendEvent.isCancelled) {
            val lastUsedWrapper = packetSendEvent.lastUsedWrapper
            if (lastUsedWrapper != null) {
                ByteBufHelper.clear(packetSendEvent.byteBuf)
                lastUsedWrapper.writeVarInt(packetSendEvent.packetId)
                lastUsedWrapper.write()
            } else {
                buffer.readerIndex(firstReaderIndex)
            }
            ctx.write(buffer, promise)
        } else {
            ReferenceCountUtil.release(packetSendEvent.byteBuf)
        }

        if (packetSendEvent.hasPostTasks()) {
            for (task in packetSendEvent.postTasks) {
                task.run()
            }
        }
    }

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg !is ByteBuf) {
            ctx.write(msg, promise)
            return
        }

        val inBuf: ByteBuf = msg

        // Handle promise management first (matches Spigot)
        val oldPromise = if (this.promise != null && this.promise?.isSuccess == false) {
            this.promise
        } else {
            null
        }

        var currentPromise = promise
        if (NETTY_4_1_0) {
            // "unvoid" will just make sure we can actually add listeners to this promise...
            // since 1.21.6, mojang will give us void promises as they don't care about the result
            currentPromise = currentPromise.unvoid()
        }

        currentPromise.addListener { _ -> this.promise = oldPromise }
        this.promise = currentPromise

        // Process the packet and execute post-send tasks (matches Spigot)
        handlePacket(ctx, inBuf, currentPromise)

        // Check for empty packets last (matches Spigot)
        if (!ByteBufHelper.isReadable(inBuf)) {
            throw CancelPacketException.INSTANCE
        } else {
            if (isPre1_20_5) {
                read(ctx, inBuf, currentPromise)
            } else {
                ctx.write(inBuf, currentPromise)
            }
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
            if (!isMinecraftServerInstanceDebugging()) {
                if (PacketEvents.getAPI().settings.isFullStackTraceEnabled) {
                    cause.printStackTrace()
                } else {
                    PacketEvents.getAPI().logManager.warn(cause.message)
                }
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

    // Placeholder for Minecraft server debugging check (Fabric-specific)
    private fun isMinecraftServerInstanceDebugging(): Boolean {
        // TODO: Implement Fabric-specific debugging check
        return false
    }

    // TODO this code is shared with bungee, it should really be in cross-platform and not duplicated
//    private fun tryFixCompressorOrder(
//        ctx: ChannelHandlerContext,
//        buffer: ByteBuf
//    ): ChannelHandlerContext {
//        if (handledCompression) {
//            return ctx
//        }
//        val pipe = ctx.pipeline()
//        val pipeNames = pipe.names()
//        if (pipeNames.contains("frame-prepender-compress")) {
//            // "modern" version, no need to handle this here
//            handledCompression = true
//            return ctx
//        }
//        val compressorIndex = pipeNames.indexOf("compress")
//        if (compressorIndex == -1) {
//            return ctx
//        }
//        handledCompression = true
//        if (compressorIndex <= pipeNames.indexOf(PacketEvents.ENCODER_NAME)) {
//            return ctx // order already seems to be correct
//        }
//
//        // relocate handlers
//        val decoder = pipe.remove(PacketEvents.DECODER_NAME)
//        val encoder = pipe.remove(PacketEvents.ENCODER_NAME)
//        pipe.addAfter(ChannelHandlers.PACKET_COMPRESSION_DECODER, PacketEvents.DECODER_NAME, decoder)
//        pipe.addAfter(ChannelHandlers.PACKET_COMPRESSION_ENCODER, PacketEvents.ENCODER_NAME, encoder)
//
//        // manually decompress packet and update context,
//        // so we don't need to additionally manually re-compress the packet
//        decompress(pipe, buffer)
//        return pipe.context(PacketEvents.ENCODER_NAME)
//    }

//    private fun decompress(pipe: ChannelPipeline, buffer: ByteBuf) {
//        val decompressor = pipe.get(ChannelHandlers.PACKET_COMPRESSION_DECODER) as CompressionDecoder
//        val decompressorCtx = pipe.context(ChannelHandlers.PACKET_COMPRESSION_DECODER)
//
//        var decompressed: ByteBuf? = null
//        try {
//            val result = FabricCustomPipelineUtil.callPacketDecodeByteBuf(
//                decompressor, decompressorCtx, buffer
//            )
//            decompressed = result[0] as ByteBuf
//            if (buffer !== decompressed) {
//                buffer.clear().writeBytes(decompressed)
//            }
//        } catch (exception: InvocationTargetException) {
//            throw RuntimeException(exception)
//        } finally {
//            ReferenceCountUtil.release(decompressed)
//        }
//    }
}
