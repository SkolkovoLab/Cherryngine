package ru.cherryngine.lib.minecraft.utils.color

import net.kyori.adventure.util.ARGBLike
import net.kyori.adventure.util.RGBLike

fun rgbLikeOf(red: Int, green: Int, blue: Int) = RGBLikeImpl(red, green, blue)
fun rgbLikeOf(rgb: Int) = RGBLikeImpl.fromInt(rgb)

fun argbLikeOf(alpha: Int, red: Int, green: Int, blue: Int) = ARGBLikeImpl(alpha, red, green, blue)
fun argbLikeOf(rgb: Int) = ARGBLikeImpl.fromInt(rgb)

fun RGBLike.asRGB(): Int {
    var rgb = red()
    rgb = (rgb shl 8) + green()
    return (rgb shl 8) + blue()
}

fun RGBLike.asARGB(): Int {
    val alpha = (this as? ARGBLike)?.alpha() ?: 255
    return (alpha shl 24) + asRGB()
}