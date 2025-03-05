package ru.cherryngine.engine.scenes.view

import ru.cherryngine.engine.scenes.api.Module

interface Viewable : Module {
    fun showFor(viewer: Viewer): Boolean
    fun hideFor(viewer: Viewer): Boolean
}