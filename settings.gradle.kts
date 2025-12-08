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
    "engine-ecs",
    "engine-integration:grim",
    "engine-integration:viaversion",
    "engine-physics",

    "lib-adventure-serializer-nbt",
    "lib-jackson",
    "lib-math",
    "lib-minecraft",
    "lib-polar",
    "lib-viaversion",
    "lib-world",
)