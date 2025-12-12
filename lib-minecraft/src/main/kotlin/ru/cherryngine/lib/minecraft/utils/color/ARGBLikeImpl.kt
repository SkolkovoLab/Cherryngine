package ru.cherryngine.lib.minecraft.utils.color


import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.util.ARGBLike
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ARGBLikeImpl(
    val alpha: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
) : ARGBLike {
    init {
        require(alpha in 0..255)
        require(red in 0..255)
        require(green in 0..255)
        require(blue in 0..255)
    }

    override fun alpha(): Int = alpha
    override fun red() = red
    override fun green() = green
    override fun blue() = blue

    companion object {
        private const val BIT_MASK = 0xff

        val NETWORK_TYPE: StreamCodec<ARGBLike> = StreamCodec.INT.transform(::fromInt, ARGBLike::asARGB)

        val CODEC: Codec<ARGBLike> = Codec.INT.transform<ARGBLike>(::fromInt, ARGBLike::asARGB)

        val STRING_CODEC: Codec<ARGBLike> = Codec.STRING.transform(
            { hex -> ShadowColor.fromHexString(hex)!! },
            { color -> ShadowColor.shadowColor(color).asHexString() }
        )

        val WHITE = ARGBLikeImpl(255, 255, 255, 255)
        val BLACK = ARGBLikeImpl(255, 0, 0, 0)
        val TRANSPARENT = ARGBLikeImpl(0, 0, 0, 0)

        fun fromARGBLike(argb: ARGBLike): ARGBLikeImpl =
            argb as? ARGBLikeImpl ?: ARGBLikeImpl(argb.alpha(), argb.red(), argb.green(), argb.blue())

        fun fromInt(argb: Int) = ARGBLikeImpl(
            (argb shr 24) and BIT_MASK,
            (argb shr 16) and BIT_MASK,
            (argb shr 8) and BIT_MASK,
            argb and BIT_MASK
        )
    }
}
