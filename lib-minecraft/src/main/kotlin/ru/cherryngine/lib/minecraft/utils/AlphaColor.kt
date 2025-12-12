package ru.cherryngine.lib.minecraft.utils


import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.util.ARGBLike
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

class AlphaColor(
    private val alpha: Int,
    red: Int,
    green: Int,
    blue: Int,
) : Color(red, green, blue), ARGBLike {

    init {
        require(alpha in 0..255)
    }

    companion object {
        private const val BIT_MASK = 0xff

        val NETWORK_TYPE: StreamCodec<ARGBLike> = StreamCodec.INT.transform(
            { AlphaColor(it) },
            { fromARGBLike(it).asARGB() }
        )

        val CODEC: Codec<ARGBLike> = Codec.INT.transform<ARGBLike>(
            { AlphaColor(it) },
            { fromARGBLike(it).asARGB() }
        )

        val STRING_CODEC: Codec<ARGBLike> = Codec.STRING.transform(
            { hex -> ShadowColor.fromHexString(hex)!! },
            { color -> ShadowColor.shadowColor(color).asHexString() }
        )

        val WHITE = AlphaColor(255, 255, 255, 255)
        val BLACK = AlphaColor(255, 0, 0, 0)
        val TRANSPARENT = AlphaColor(0, 0, 0, 0)

        fun fromARGBLike(argb: ARGBLike): AlphaColor =
            argb as? AlphaColor ?: AlphaColor(argb.alpha(), argb.red(), argb.green(), argb.blue())
    }

    constructor(alpha: Float, red: Float, green: Float, blue: Float) :
            this(
                (alpha * 255).toInt(),
                (red * 255).toInt(),
                (green * 255).toInt(),
                (blue * 255).toInt()
            )

    constructor(argb: Int) :
            this(
                (argb shr 24) and BIT_MASK,
                (argb shr 16) and BIT_MASK,
                (argb shr 8) and BIT_MASK,
                argb and BIT_MASK
            )

    constructor(alpha: Int, rgb: RGBLike) :
            this(alpha, rgb.red(), rgb.green(), rgb.blue())

    override fun withRed(r: Int): AlphaColor =
        AlphaColor(alpha, r, green(), blue())

    override fun withGreen(g: Int): AlphaColor =
        AlphaColor(alpha, red(), g, blue())

    override fun withBlue(b: Int): AlphaColor =
        AlphaColor(alpha, red(), green(), b)

    override fun withAlpha(alpha: Int): AlphaColor =
        AlphaColor(alpha, red(), green(), blue())

    fun asARGB(): Int =
        (alpha shl 24) + asRGB()

    override fun alpha(): Int = alpha

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        other as AlphaColor
        return this.alpha == other.alpha &&
                this.red() == other.red() &&
                this.green() == other.green() &&
                this.blue() == other.blue()
    }

    override fun hashCode(): Int =
        Objects.hash(alpha, red(), green(), blue())

    override fun toString(): String =
        "AlphaColor[alpha=$alpha, red=${red()}, green=${green()}, blue=${blue()}]"
}
