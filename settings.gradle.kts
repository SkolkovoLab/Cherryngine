pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("build-logic")
}

rootProject.name = "Cherryngine"

include(
    "engine-core",
    "engine-scenes",

    "impl-demo",

    "lib-kotlinx",
    "lib-math",
)
