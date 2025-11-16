package ru.cherryngine.engine.core

import io.micronaut.runtime.Micronaut
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

object Main {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        SLF4JBridgeHandler.removeHandlersForRootLogger()
        SLF4JBridgeHandler.install()
        printBanner()
        logger.info("Starting server")

        try {
            measureTimeMillis {
                Micronaut.build(*args)
                    .banner(false)
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

    fun printBanner() {
        """
          ____  _ ___ ___ _____   ____  _  __ _ __  _ ___  
         / _/ || | __| _ \ _ \ `v' /  \| |/ _] |  \| | __| 
        | \_| >< | _|| v / v /`. .'| | ' | [/\ | | ' | _|  
         \__/_||_|___|_|_\_|_\ !_! |_|\__|\__/_|_|\__|___| 
        """.trimIndent().lineSequence().forEach(::println)
    }
}