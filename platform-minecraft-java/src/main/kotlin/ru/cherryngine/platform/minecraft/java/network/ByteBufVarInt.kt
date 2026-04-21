package ru.cherryngine.platform.minecraft.java.network

import io.netty.buffer.ByteBuf

object ByteBufVarInt {
    fun read(buffer: ByteBuf): Int {
        var result = 0
        var shift = 0
        while (true) {
            val b = buffer.readByte().toInt()
            result = result or ((b and 0x7F) shl shift)
            if (b and 0x80 == 0) return result
            shift += 7
            if (shift >= 32) throw RuntimeException("VarInt is too big")
        }
    }

    fun write(buffer: ByteBuf, value: Int) {
        var v = value
        while (true) {
            if (v and 0x7F.inv() == 0) {
                buffer.writeByte(v)
                return
            }
            buffer.writeByte(v and 0x7F or 0x80)
            v = v ushr 7
        }
    }
}
