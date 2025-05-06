plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-math"))

    api(libs.bundles.jackson)

    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)

    api(libs.typetools)
}
