plugins {
    id("cherryngine-micronaut-base")
    id("io.micronaut.minimal.application")
}

application {
    mainClass = "ru.cherryngine.engine.resourcepack.rpgen.Main"
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

    val runDir = "build/generated/resourcepack"
    val outputDir = "$runDir/outputs"

    val runTaskProvider = named<JavaExec>("run") {
        workingDir = projectDir.resolve(runDir).apply {
            mkdirs()
        }
        standardInput = System.`in`
    }

    register("generateResourcepack") {
        val runTask = runTaskProvider.get()
        inputs.files(runTask.inputs.files) // инпуты аналогичны таске run, так что тупа спиздим оттудова
        val obfuscate: Boolean = System.getenv("RPGEN_OBFUSCATE")?.toBoolean() ?: false
        val obfuscationSalt: String = System.getenv("RPGEN_OBFUSCATION_SALT") ?: ""
        inputs.property("obfuscate", obfuscate)
        inputs.property("obfuscationSalt", obfuscationSalt)

        outputs.dir(projectDir.resolve(outputDir))

        doLast {
            runTask.actions.forEach { action ->
                action.execute(runTask)
            }
        }
    }
}