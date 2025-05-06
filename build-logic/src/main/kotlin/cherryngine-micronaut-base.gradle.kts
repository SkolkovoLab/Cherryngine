import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("cherryngine-kotlin")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("com.google.devtools.ksp")
    id("com.gradleup.shadow")
}

val libs = the<LibrariesForLibs>()

dependencies {
    api(platform(libs.micronaut.bom))
    api(libs.micronaut.kotlin.runtime)
    api(libs.micronaut.serde.jackson)

    ksp(libs.micronaut.serde.processor)

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
