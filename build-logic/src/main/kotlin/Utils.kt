import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.language.jvm.tasks.ProcessResources

fun DependencyHandler.engine(dependencyNotation: String): String {
    val list = listOf("Cherryngine") + dependencyNotation.split(":").drop(1)
    return "${list.dropLast(1).joinToString(".")}:${list.last()}"
}

fun ProcessResources.addResourcePack(rpProj: Project) {
    val generateResourcepack = rpProj.tasks.named("generateResourcepack")
    dependsOn(generateResourcepack)
    from(generateResourcepack.get().outputs.files) {
        into("generated")
    }
}