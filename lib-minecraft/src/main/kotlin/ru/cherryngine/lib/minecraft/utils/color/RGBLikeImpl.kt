package ru.cherryngine.lib.minecraft.utils.color

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class RGBLikeImpl(
    val red: Int,
    val green: Int,
    val blue: Int,
) : RGBLike {
    init {
        require(red in 0..255)
        require(green in 0..255)
        require(blue in 0..255)
    }

    override fun red() = red
    override fun green() = green
    override fun blue() = blue

    companion object {
        private const val BIT_MASK = 0xff

        val NETWORK_TYPE: StreamCodec<RGBLike> = StreamCodec.INT.transform(::fromInt, RGBLike::asRGB)

        val RGB_BYTE_NETWORK_TYPE = object : StreamCodec<RGBLike> {
            override fun write(buffer: ByteBuf, value: RGBLike) {
                StreamCodec.BYTE.write(buffer, value.red().toByte())
                StreamCodec.BYTE.write(buffer, value.green().toByte())
                StreamCodec.BYTE.write(buffer, value.blue().toByte())
            }

            override fun read(buffer: ByteBuf): RGBLike {
                val r = StreamCodec.BYTE.read(buffer).toInt()
                val g = StreamCodec.BYTE.read(buffer).toInt()
                val b = StreamCodec.BYTE.read(buffer).toInt()
                return RGBLikeImpl(r, g, b)
            }
        }

        val CODEC: Codec<RGBLike> = Codec.INT.transform<RGBLike>(::fromInt, RGBLike::asRGB)

        val STRING_CODEC: Codec<RGBLike> =
            Codec.STRING.transform(
                { hex -> requireNotNull(TextColor.fromHexString(hex)) },
                { color -> TextColor.color(color).asHexString() }
            )

        val WHITE: RGBLike = RGBLikeImpl(255, 255, 255)
        val BLACK: RGBLike = RGBLikeImpl(0, 0, 0)

        fun fromRGBLike(rgb: RGBLike): RGBLikeImpl =
            rgb as? RGBLikeImpl ?: RGBLikeImpl(rgb.red(), rgb.green(), rgb.blue())

        fun fromInt(rgb: Int) = RGBLikeImpl(
            (rgb shr 16) and BIT_MASK,
            (rgb shr 8) and BIT_MASK,
            rgb and BIT_MASK
        )
    }
}
