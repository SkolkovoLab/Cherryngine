plugins {
    id("cherryngine-java")
}

dependencies {
    api("net.kyori:option:1.1.0")
    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.nbt)
    compileOnlyApi("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation(libs.kyori.adventure.text.serializer.commons)
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")
}