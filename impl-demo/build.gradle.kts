plugins {
    id("cherryngine-micronaut-app")
}

dependencies {
    api(project(":engine-core"))
    api(project(":lib-viaversion"))
    implementation("com.github.luben:zstd-jni:1.5.5-3") // for polar
    api("io.github.quillraven.fleks:Fleks:2.12")
}
