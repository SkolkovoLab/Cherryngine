package ru.cherryngine.lib.minecraft.utils

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

open class Color(
    private val red: Int,
    private val green: Int,
    private val blue: Int,
) : RGBLike {

    init {
        require(red in 0..255)
        require(green in 0..255)
        require(blue in 0..255)
    }

    companion object {
        private const val BIT_MASK = 0xff

        val NETWORK_TYPE: StreamCodec<RGBLike> =
            StreamCodec.INT.transform(
                { Color(it) },
                { fromRGBLike(it).asRGB() }
            )

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
                return Color(r, g, b)
            }
        }

        val CODEC: Codec<RGBLike> =
            Codec.INT.transform<RGBLike>(
                { Color(it) },
                { fromRGBLike(it).asRGB() }
            )

        val STRING_CODEC: Codec<RGBLike> =
            Codec.STRING.transform(
                { hex -> requireNotNull(TextColor.fromHexString(hex)) },
                { color -> TextColor.color(color).asHexString() }
            )

        val WHITE: RGBLike = Color(255, 255, 255)
        val BLACK: RGBLike = Color(0, 0, 0)

        fun fromRGBLike(rgb: RGBLike): Color =
            if (rgb is Color) rgb else Color(rgb.red(), rgb.green(), rgb.blue())
    }

    constructor(red: Float, green: Float, blue: Float) :
            this((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())

    constructor(rgb: Int) : this(
        (rgb shr 16) and BIT_MASK,
        (rgb shr 8) and BIT_MASK,
        rgb and BIT_MASK
    )

    constructor(rgb: RGBLike) : this(rgb.red(), rgb.green(), rgb.blue())

    open fun withRed(r: Int) = Color(r, green, blue)
    open fun withGreen(g: Int) = Color(red, g, blue)
    open fun withBlue(b: Int) = Color(red, green, b)

    open fun withAlpha(alpha: Int): AlphaColor = AlphaColor(alpha, red, green, blue)

    fun asRGB(): Int {
        var rgb = red
        rgb = (rgb shl 8) + green
        return (rgb shl 8) + blue
    }

    fun mixWith(vararg colors: RGBLike): Color {
        var r = red
        var g = green
        var b = blue

        var maxComp = max(max(r, g), b)

        for (col in colors) {
            r += col.red()
            g += col.green()
            b += col.blue()
            maxComp += max(max(col.red(), col.green()), col.blue())
        }

        val count = colors.size + 1f

        val avgR = r / count
        val avgG = g / count
        val avgB = b / count
        val avgMax = maxComp / count

        val maxAvg = max(max(avgR, avgG), avgB)
        val gain = avgMax / maxAvg

        return Color(
            (avgR * gain).roundToInt(),
            (avgG * gain).roundToInt(),
            (avgB * gain).roundToInt()
        )
    }

    @Deprecated("Use red()")
    fun getRed() = red

    @Deprecated("Use green()")
    fun getGreen() = green

    @Deprecated("Use blue()")
    fun getBlue() = blue

    override fun red() = red
    override fun green() = green
    override fun blue() = blue

    override fun equals(other: Any?): Boolean =
        other is Color &&
                red == other.red &&
                green == other.green &&
                blue == other.blue

    override fun hashCode(): Int = Objects.hash(red, green, blue)

    override fun toString(): String =
        "Color[red=$red, green=$green, blue=$blue]"
}
