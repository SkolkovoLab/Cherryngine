package ru.cherryngine.lib.packetevents.handlers

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.EventCreationUtil
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import ru.cherryngine.lib.minecraft.server.ChannelHandlers
import ru.cherryngine.lib.minecraft.server.Connection

@ChannelHandler.Sharable
class PacketEventsEncoder(
    var user: User,
) : MessageToByteEncoder<ByteBuf>() {
    var player: Connection? = null

    @Throws(Exception::class)
    fun read(ctx: ChannelHandlerContext, buffer: ByteBuf) {
        val firstReaderIndex = buffer.readerIndex()

        val packetSendEvent = EventCreationUtil.createSendEvent(
            ctx.channel(), user, player, buffer, false
        )

        val savedReaderIndex = buffer.readerIndex()
        PacketEvents.getAPI().eventManager.callEvent(packetSendEvent) {
            buffer.readerIndex(savedReaderIndex)
        }

        if (!packetSendEvent.isCancelled) {
            val lastUsedWrapper = packetSendEvent.lastUsedWrapper
            if (lastUsedWrapper != null) {
                ByteBufHelper.clear(packetSendEvent.byteBuf)
                lastUsedWrapper.writeVarInt(packetSendEvent.packetId)
                lastUsedWrapper.write()
            }

            buffer.readerIndex(firstReaderIndex)
        } else {
            ByteBufHelper.clear(packetSendEvent.byteBuf)
        }

        if (packetSendEvent.hasPostTasks()) {
            packetSendEvent.postTasks.forEach { it.run() }
        }
    }

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        if (!msg.isReadable) return
        val connection = ctx.pipeline().get(ChannelHandlers.PLAYER_NETWORK_MANAGER) as Connection
        handleClientBoundPacket(ctx.channel(), user, connection, msg)

        val transformed = ctx.alloc().buffer().writeBytes(msg)
        try {
            read(ctx, transformed)
            out.writeBytes(transformed)
        } finally {
            transformed.release()
        }
    }

    private fun handleClientBoundPacket(
        channel: Channel,
        user: User,
        player: Any,
        buffer: ByteBuf,
    ): PacketSendEvent? {
        val packetSendEvent = PacketEventsImplHelper.handleClientBoundPacket(channel, user, player, buffer, true)
        if (packetSendEvent != null && packetSendEvent.hasTasksAfterSend()) {
            for (task in packetSendEvent.getTasksAfterSend()) {
                task.run()
            }
        }
        return packetSendEvent
    }
}
