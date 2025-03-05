version = "dev"

tasks.register("clean") {
    group = "build"
    description = "Cleans all modules by invoking their clean tasks."
    dependsOn(subprojects.map { it.tasks.named("clean") })
}
