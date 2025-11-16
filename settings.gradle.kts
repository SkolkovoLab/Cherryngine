pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("build-logic")
}

rootProject.name = "Cherryngine"

include(
    "engine-core",
    "engine-integration:grim",
    "engine-integration:viaversion",

    "impl-demo",

    "lib-jackson",
    "lib-math",
    "lib-minecraft",
    "lib-viaversion",
)