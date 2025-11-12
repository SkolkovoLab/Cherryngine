package ru.cherryngine.impl.demo

import ru.cherryngine.impl.demo.view.StaticViewable
import ru.cherryngine.impl.demo.view.Viewable
import ru.cherryngine.lib.minecraft.server.Connection

class Player(
    val connection: Connection,
) {
    val uuid = connection.gameProfile.uuid
    val currentVisibleViewables: MutableSet<Viewable> = hashSetOf()
    val currentVisibleStaticViewables: MutableSet<StaticViewable> = hashSetOf()
}