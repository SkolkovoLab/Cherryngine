plugins {
    id("cherryngine-micronaut-base")
    id("io.micronaut.minimal.application")
}

application {
    mainClass = "ru.cherryngine.engine.core.Main"
}

tasks {
    distZip {
        enabled = false
    }
    distTar {
        enabled = false
    }
    shadowDistTar {
        enabled = false
    }
    shadowDistZip {
        enabled = false
    }
    shadowJar {
        mergeServiceFiles()
        isZip64 = true
    }

    named<JavaExec>("run") {
        workingDir = projectDir.resolve("run/").apply {
            mkdirs()
            resolve("application.yml").apply { if (!exists()) createNewFile() }
        }
        jvmArgs = listOf("-Dmicronaut.config.files=application.yml")
        standardInput = System.`in`
    }
}