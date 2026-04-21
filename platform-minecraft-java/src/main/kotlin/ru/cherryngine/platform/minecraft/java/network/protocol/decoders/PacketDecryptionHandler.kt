package ru.cherryngine.platform.minecraft.java.network.protocol.decoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import ru.cherryngine.platform.minecraft.java.network.protocol.cryptography.EncryptionBase
import ru.cherryngine.platform.minecraft.java.network.protocol.cryptography.EncryptionUtil
import ru.cherryngine.platform.minecraft.java.network.protocol.cryptography.PlayerCrypto


class PacketDecryptionHandler(
    private val playerCrypto: PlayerCrypto,
) : MessageToMessageDecoder<ByteBuf>() {
    private val encryptionBase = EncryptionBase(EncryptionUtil.getDecryptionCipherInstance(playerCrypto))

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        if (!playerCrypto.isConnectionEncrypted) {
            out.add(msg.retain())
            return
        }
        val byteBuf = encryptionBase.decrypt(ctx, msg.retain())
        out.add(byteBuf)
        msg.release()
    }
}