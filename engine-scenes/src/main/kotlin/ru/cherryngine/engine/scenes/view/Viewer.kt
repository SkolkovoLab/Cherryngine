package ru.cherryngine.engine.scenes.view

import ru.cherryngine.engine.scenes.api.Module

interface Viewer : Module {
    fun show(viewable: Viewable): Boolean { return true }
    fun hide(viewable: Viewable): Boolean { return true }
}