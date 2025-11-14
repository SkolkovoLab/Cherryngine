package ru.cherryngine.lib.viaversion

import com.viaversion.viaversion.api.ViaAPI
import com.viaversion.viaversion.api.configuration.ViaVersionConfig
import com.viaversion.viaversion.api.platform.PlatformTask
import com.viaversion.viaversion.api.platform.ViaPlatform
import com.viaversion.viaversion.util.VersionInfo
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import java.io.File
import java.util.logging.Logger as JulLogger

class ViaPlatformImpl(
    private val config: ViaVersionConfig,
    private val api: ViaAPI<Connection>,
) : ViaPlatform<Connection> {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val julLogger = Slf4jToJulAdapter(logger)
    override fun getLogger(): JulLogger = julLogger
    override fun getApi(): ViaAPI<Connection> = api
    override fun getConf(): ViaVersionConfig = config
    override fun getPlatformName() = "Cherryngine"
    override fun getPlatformVersion() = "dev"
    override fun getPluginVersion() = VersionInfo.VERSION
    override fun getDataFolder() = File("./viaversion/")
    override fun hasPlugin(plugin: String) = false

    override fun runAsync(runnable: Runnable) =
        run(runnable, delayTicks = 0, periodTicks = 0)

    override fun runRepeatingAsync(runnable: Runnable, periodTicks: Long) =
        run(runnable, delayTicks = 0, periodTicks = periodTicks)

    override fun runSync(runnable: Runnable) =
        run(runnable, delayTicks = 0, periodTicks = 0)

    override fun runSync(runnable: Runnable, delayTicks: Long) =
        run(runnable, delayTicks = delayTicks, periodTicks = 0)

    override fun runRepeatingSync(runnable: Runnable, periodTicks: Long) =
        run(runnable, delayTicks = 0, periodTicks = periodTicks)

    private fun run(runnable: Runnable, delayTicks: Long = 0, periodTicks: Long = 0): PlatformTask<Job> {
        val job = CoroutineScope(Dispatchers.IO).launch {
            if (delayTicks > 0) delay(delayTicks * 50)
            while (true) {
                runnable.run()
                if (periodTicks <= 0) break
                delay(periodTicks * 50)
            }
        }
        return PlatformTask<Job> { job.cancel() }
    }
}