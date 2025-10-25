plugins {
    id("cherryngine-micronaut-app")
}

dependencies {
    api(project(":engine-core"))
    implementation("com.github.luben:zstd-jni:1.5.5-3") // for polar
}
