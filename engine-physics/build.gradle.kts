plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))

    implementation("com.github.stephengold:jolt-jni-Windows64:4.3.0")
    runtimeOnly("com.github.stephengold:jolt-jni-Linux64:4.3.0:ReleaseSp")
    runtimeOnly("com.github.stephengold:jolt-jni-Windows64:4.3.0:ReleaseSp")
    implementation("io.github.electrostat-lab:snaploader:1.1.1-stable")
    runtimeOnly("com.github.oshi:oshi-core:6.9.1")

    implementation("org.joml:joml:1.10.8")
}
