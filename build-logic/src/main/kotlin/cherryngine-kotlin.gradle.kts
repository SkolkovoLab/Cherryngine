import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("cherryngine-java")
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions.freeCompilerArgs = listOf(
        "-Xcontext-parameters",
        "-opt-in=kotlin.time.ExperimentalTime"
    )
}
