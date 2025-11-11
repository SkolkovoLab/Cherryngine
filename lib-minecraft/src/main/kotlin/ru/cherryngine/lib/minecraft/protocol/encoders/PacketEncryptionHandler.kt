package ru.cherryngine.lib.minecraft.protocol.encoders

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import ru.cherryngine.lib.minecraft.protocol.cryptography.EncryptionBase
import ru.cherryngine.lib.minecraft.protocol.cryptography.EncryptionUtil
import ru.cherryngine.lib.minecraft.protocol.cryptography.PlayerCrypto

class PacketEncryptionHandler(
    private val playerCrypto: PlayerCrypto,
) : MessageToByteEncoder<ByteBuf>() {
    private val encryptionBase = EncryptionBase(EncryptionUtil.getEncryptionCipherInstance(playerCrypto))

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        if (!playerCrypto.isConnectionEncrypted) {
            out.writeBytes(msg.retain())
            return
        }

        out.writeBytes(encryptionBase.encrypt(msg.retain()))
        msg.release()
    }
}