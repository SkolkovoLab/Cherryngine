@file:Suppress("unused")

package ru.cherryngine.engine.core.ext

import net.minestom.server.coordinate.Pos
import ru.cherryngine.lib.math.View

fun Pos.asView() = View(yaw, pitch)
fun Pos.asQRot() = asView().getRotation()