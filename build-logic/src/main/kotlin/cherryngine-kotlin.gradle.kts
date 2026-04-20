import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("cherryngine-java")
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs = listOf(
        "-Xcontext-parameters",
        "-opt-in=kotlin.time.ExperimentalTime"
    )
}
