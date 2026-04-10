plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)
    api(libs.kyori.adventure.text.serializer.plain)

    implementation(libs.slf4j.bridge.jul)
}
