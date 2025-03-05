@file:Suppress("unused")

package ru.cherryngine.engine.scenes.models.builder

import ru.cherryngine.engine.scenes.models.Models

fun skeleton(init: SkeletonModelBuilder.() -> Unit): Models.SkeletonModel {
    val builder = SkeletonModelBuilder()
    builder.init()
    return builder.build()
}

fun free(init: FreeModelBuilder.() -> Unit): Models.FreeModel {
    val builder = FreeModelBuilder()
    builder.init()
    return builder.build()
}