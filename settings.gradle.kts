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
//    "engine-integration:grim", // FIXME Could not resolve com.github.retrooper:packetevents-api:2.10.2+8c92928-SNAPSHOT.
    "engine-integration:viaversion",

    "impl-demo",

    "lib-jackson",
    "lib-math",
    "lib-minecraft",
    "lib-polar",
    "lib-viaversion",
)