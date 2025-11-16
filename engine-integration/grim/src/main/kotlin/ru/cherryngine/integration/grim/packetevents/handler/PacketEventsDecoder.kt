package ru.cherryngine.integration.grim.packetevents.handler

import ac.grim.grimac.utils.viaversion.ViaVersionUtil
import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.protocol.PacketSide
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.util.PacketEventsImplHelper
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import ru.cherryngine.lib.minecraft.server.Connection

@ChannelHandler.Sharable
class PacketEventsDecoder(
    side: PacketSide,
    var user: User,
    private val preViaVersion: Boolean
) : MessageToMessageDecoder<ByteBuf>() {

    private val side: PacketSide = side.opposite
    var player: Connection? = null

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        if (!msg.isReadable) return

        val api = PacketEvents.getAPI()

        // Pre-Via listeners
        if (!preViaVersion &&
            api.settings.isPreViaInjection &&
            !ViaVersionUtil.isAvailable
        ) {
            PacketEventsImplHelper.handleServerBoundPacket(
                ctx.channel(), user, player, msg, false
            )
        }

        PacketEventsImplHelper.handlePacket(
            ctx.channel(),
            user,
            player,
            msg,
            !preViaVersion,
            side
        )

        if (msg.isReadable) {
            out.add(msg.retain())
        }
    }
}
