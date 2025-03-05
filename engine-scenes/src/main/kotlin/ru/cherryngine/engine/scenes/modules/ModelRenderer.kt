package ru.cherryngine.engine.scenes.modules

import io.micronaut.context.annotation.Parameter
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.models.Models
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.engine.scenes.view.Viewable
import ru.cherryngine.engine.scenes.view.Viewer

@ModulePrototype
abstract class ModelRenderer(
    @Parameter override val gameObject: GameObject
) : Module, Viewable {

    abstract val model: Models

    override fun showFor(viewer: Viewer): Boolean {
        return when {
            viewer is ClientModule -> {
                whenShow()
                model.render(viewer.connection)
                true
            }
            else -> viewer.show(this)
        }
    }

    override fun hideFor(viewer: Viewer): Boolean {
        return when {
            viewer is ClientModule -> {
                whenHide()
                model.render(viewer.connection)
                true
            }
            else -> viewer.hide(this)
        }
    }


    open fun whenShow() {}
    open fun whenHide() {}
}