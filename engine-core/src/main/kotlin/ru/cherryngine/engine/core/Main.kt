package ru.cherryngine.engine.core

import io.micronaut.runtime.Micronaut
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

object Main {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("Starting server")

        try {
            measureTimeMillis {
                Micronaut.build(*args)
                    .eagerInitSingletons(true)
                    .start()
            }.let {
                logger.info("Server started in ${"%.2f".format(it / 1000.0)} sec.")
            }
        } catch (ex: Exception) {
            logger.error("Server startup error", ex)
            exitProcess(1)
        }
    }
}