plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${libs.versions.kotlin.get()}")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${libs.versions.ksp.get()}")
    implementation("io.micronaut.gradle:micronaut-minimal-plugin:${libs.versions.micronaut.plugin.get()}")
    implementation("io.micronaut.gradle:micronaut-docker-plugin:${libs.versions.micronaut.plugin.get()}")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:${libs.versions.shadow.get()}")
}
