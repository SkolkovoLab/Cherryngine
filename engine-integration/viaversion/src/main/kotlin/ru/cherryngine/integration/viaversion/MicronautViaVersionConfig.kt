package ru.cherryngine.integration.viaversion

import com.viaversion.viaversion.api.Via
import com.viaversion.viaversion.api.configuration.RateLimitConfig
import com.viaversion.viaversion.api.configuration.ViaVersionConfig
import com.viaversion.viaversion.api.minecraft.WorldIdentifiers
import com.viaversion.viaversion.api.protocol.version.BlockedProtocolVersions
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion
import com.viaversion.viaversion.libs.gson.JsonElement
import com.viaversion.viaversion.protocol.BlockedProtocolVersionsImpl
import io.micronaut.core.value.PropertyResolver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

@Suppress("removal", "OVERRIDE_DEPRECATION")
@Singleton
class MicronautViaVersionConfig(
    propertyResolver: PropertyResolver,
) : ViaVersionConfig {
    private val logger = LoggerFactory.getLogger(MicronautViaVersionConfig::class.java)

    private val resolverWrapper = PropertyResolverWrapper(propertyResolver, "viaversion")

    private fun loadBlockedProtocolVersions(): BlockedProtocolVersions {
        val blockProtocols = resolverWrapper.getStringList("block-protocols", listOf())
            .map { ProtocolVersion.getProtocol(it.toInt()) }
            .toMutableSet()

        val blockVersions = resolverWrapper.getStringList("block-versions", listOf())

        var lowerBound = ProtocolVersion.unknown
        var upperBound = ProtocolVersion.unknown

        for (s in blockVersions) {
            if (s.isEmpty()) continue

            when (s[0]) {
                '<', '>' -> {
                    val protocolVersion = protocolVersion(s.substring(1)) ?: continue
                    if (s[0] == '<') {
                        if (lowerBound.isKnown) {
                            logger.warn("Already set lower bound $lowerBound overridden by ${protocolVersion.name}")
                        }
                        lowerBound = protocolVersion
                    } else {
                        if (upperBound.isKnown) {
                            logger.warn("Already set upper bound $upperBound overridden by ${protocolVersion.name}")
                        }
                        upperBound = protocolVersion
                    }
                }

                else -> {
                    val protocolVersion = protocolVersion(s) ?: continue
                    if (!blockProtocols.add(protocolVersion)) {
                        logger.warn("Duplicated blocked protocol version $protocolVersion")
                    }
                }
            }
        }

        // Проверяем дубли с учетом диапазонов
        if (lowerBound.isKnown || upperBound.isKnown) {
            val finalLowerBound = lowerBound
            val finalUpperBound = upperBound
            blockProtocols.removeIf { version ->
                val coveredByLower = finalLowerBound.isKnown && version.olderThan(finalLowerBound)
                val coveredByUpper = finalUpperBound.isKnown && version.newerThan(finalUpperBound)
                if (coveredByLower || coveredByUpper) {
                    logger.warn("Blocked protocol version $version already covered by upper or lower bound")
                    true
                } else {
                    false
                }
            }
        }

        return BlockedProtocolVersionsImpl(blockProtocols, lowerBound, upperBound)
    }

    private fun protocolVersion(s: String): ProtocolVersion? {
        val protocolVersion = ProtocolVersion.getClosest(s)
        if (protocolVersion == null) {
            logger.warn("Unknown protocol version in block-versions: $s")
            return null
        }
        return protocolVersion
    }

    private fun PropertyResolverWrapper.loadRateLimitConfig(
        placeholder: String,
        countMultiplier: Int,
    ): RateLimitConfig {
        val maxPerSecond = getInt("max-per-second", -1)
        val sustainedMaxPerSecond = getInt("sustained-max-per-second", -1)

        return RateLimitConfig(
            getBoolean("enabled", true),
            if (maxPerSecond != -1) maxPerSecond * countMultiplier else -1,
            getString("max-per-second-kick-message", "You are sending too many packets!"),
            if (sustainedMaxPerSecond != -1) sustainedMaxPerSecond * countMultiplier else -1,
            getInt("sustained-threshold", 3),
            TimeUnit.SECONDS.toNanos(getInt("sustained-period-seconds", 6).toLong()),
            getString("sustained-kick-message", "You are sending too many packets, :("),
            placeholder
        )
    }


    // --- Базовые поля ---
    override fun isCheckForUpdates(): Boolean {
        return resolverWrapper.getBoolean("check-for-updates", true)
    }

    override fun isPreventCollision(): Boolean {
        return resolverWrapper.getBoolean("prevent-collision", true)
    }

    override fun isNewEffectIndicator(): Boolean {
        return resolverWrapper.getBoolean("use-new-effect-indicator", true)
    }

    override fun isShowNewDeathMessages(): Boolean {
        return false
    }

    override fun isSuppressMetadataErrors(): Boolean {
        return resolverWrapper.getBoolean("suppress-metadata-errors", false)
    }

    override fun isShieldBlocking(): Boolean {
        return resolverWrapper.getBoolean("shield-blocking", true)
    }

    override fun isNoDelayShieldBlocking(): Boolean {
        return resolverWrapper.getBoolean("no-delay-shield-blocking", false)
    }

    override fun isShowShieldWhenSwordInHand(): Boolean {
        return resolverWrapper.getBoolean("show-shield-when-sword-in-hand", false)
    }

    override fun isHologramPatch(): Boolean {
        return resolverWrapper.getBoolean("hologram-patch", false)
    }

    override fun isPistonAnimationPatch(): Boolean {
        return resolverWrapper.getBoolean("piston-animation-patch", false)
    }

    override fun isBossbarPatch(): Boolean {
        return resolverWrapper.getBoolean("bossbar-patch", true)
    }

    override fun isBossbarAntiflicker(): Boolean {
        return resolverWrapper.getBoolean("bossbar-anti-flicker", false)
    }

    override fun getHologramYOffset(): Double {
        return resolverWrapper.getDouble("hologram-y", -0.96)
    }

    override fun isAutoTeam(): Boolean {
        return isPreventCollision && resolverWrapper.getBoolean("auto-team", true)
    }

    override fun getMaxPPS(): Int {
        return resolverWrapper.getInt("max-pps", 800)
    }

    override fun getMaxPPSKickMessage(): String {
        return resolverWrapper.getString("max-pps-kick-message", "Too many packets!")
    }

    override fun getTrackingPeriod(): Int {
        return resolverWrapper.getInt("tracking-period", 5)
    }

    override fun getWarningPPS(): Int {
        return resolverWrapper.getInt("warning-pps", 400)
    }

    override fun getMaxWarnings(): Int {
        return resolverWrapper.getInt("max-warnings", 3)
    }

    override fun getMaxWarningsKickMessage(): String {
        return resolverWrapper.getString("max-warnings-kick-message", "Too many packet warnings!")
    }

    override fun getPacketTrackerConfig(): RateLimitConfig {
        return resolverWrapper.section("packet-limiter").loadRateLimitConfig("%pps", 1)
    }

    override fun getPacketSizeTrackerConfig(): RateLimitConfig {
        return resolverWrapper.section("packet-size-limiter").loadRateLimitConfig("%bps", 1024)
    }

    override fun isSendSupportedVersions(): Boolean {
        return resolverWrapper.getBoolean("send-supported-versions", false)
    }

    override fun isSimulatePlayerTick(): Boolean {
        return resolverWrapper.getBoolean("simulate-pt", true)
    }

    override fun isItemCache(): Boolean {
        return false
    }

    override fun isNMSPlayerTicking(): Boolean {
        return false
    }

    override fun isReplacePistons(): Boolean {
        return resolverWrapper.getBoolean("replace-pistons", false)
    }

    override fun getPistonReplacementId(): Int {
        return resolverWrapper.getInt("replacement-piston-id", 0)
    }

    override fun isChunkBorderFix(): Boolean {
        return resolverWrapper.getBoolean("chunk-border-fix", false)
    }

    override fun is1_13TeamColourFix(): Boolean {
        return resolverWrapper.getBoolean("team-colour-fix", true)
    }

    override fun shouldRegisterUserConnectionOnJoin(): Boolean {
        return false
    }

    override fun is1_12QuickMoveActionFix(): Boolean {
        return false
    }

    override fun blockedProtocolVersions(): BlockedProtocolVersions {
        return loadBlockedProtocolVersions()
    }

    override fun getBlockedDisconnectMsg(): String {
        return resolverWrapper.getString("block-disconnect-msg", "You are using an unsupported Minecraft version!")
    }

    override fun logBlockedJoins(): Boolean {
        return resolverWrapper.getBoolean("log-blocked-joins", false)
    }

    override fun getReloadDisconnectMsg(): String {
        return resolverWrapper.getString("reload-disconnect-msg", "Server reload, please rejoin!")
    }

    override fun isSuppressConversionWarnings(): Boolean {
        return resolverWrapper.getBoolean("suppress-conversion-warnings", false) && !Via.getManager().isDebug()
    }

    override fun isSuppressTextComponentConversionWarnings(): Boolean {
        return resolverWrapper.getBoolean("suppress-text-component-conversion-warnings", true) && !Via.getManager()
            .isDebug()
    }

    override fun isDisable1_13AutoComplete(): Boolean {
        return resolverWrapper.getBoolean("disable-1_13-auto-complete", false)
    }

    override fun isServersideBlockConnections(): Boolean {
        return resolverWrapper.getBoolean("serverside-blockconnections", true)
    }

    override fun getBlockConnectionMethod(): String {
        return "packet"
    }

    override fun isReduceBlockStorageMemory(): Boolean {
        return resolverWrapper.getBoolean("reduce-blockstorage-memory", false)
    }

    override fun isStemWhenBlockAbove(): Boolean {
        return resolverWrapper.getBoolean("flowerstem-when-block-above", false)
    }

    override fun isVineClimbFix(): Boolean {
        return resolverWrapper.getBoolean("vine-climb-fix", false)
    }

    override fun isSnowCollisionFix(): Boolean {
        return resolverWrapper.getBoolean("fix-low-snow-collision", false)
    }

    override fun isInfestedBlocksFix(): Boolean {
        return resolverWrapper.getBoolean("fix-infested-block-breaking", true)
    }

    override fun get1_13TabCompleteDelay(): Int {
        return resolverWrapper.getInt("1_13-tab-complete-delay", 0)
    }

    override fun isTruncate1_14Books(): Boolean {
        return resolverWrapper.getBoolean("truncate-1_14-books", false)
    }

    override fun isLeftHandedHandling(): Boolean {
        return resolverWrapper.getBoolean("left-handed-handling", true)
    }

    override fun is1_9HitboxFix(): Boolean {
        return false
    }

    override fun is1_14HitboxFix(): Boolean {
        return false
    }

    override fun isNonFullBlockLightFix(): Boolean {
        return resolverWrapper.getBoolean("fix-non-full-blocklight", false)
    }

    override fun is1_14HealthNaNFix(): Boolean {
        return resolverWrapper.getBoolean("fix-1_14-health-nan", true)
    }

    override fun is1_15InstantRespawn(): Boolean {
        return resolverWrapper.getBoolean("use-1_15-instant-respawn", false)
    }

    override fun isIgnoreLong1_16ChannelNames(): Boolean {
        return resolverWrapper.getBoolean("ignore-long-1_16-channel-names", true)
    }

    override fun isForcedUse1_17ResourcePack(): Boolean {
        return resolverWrapper.getBoolean("forced-use-1_17-resource-pack", false)
    }

    override fun get1_17ResourcePackPrompt(): JsonElement? {
        return null
    }

    override fun get1_16WorldNamesMap(): WorldIdentifiers {
        val resolverWrapper = resolverWrapper.section("map-1_16-world-names")
        return WorldIdentifiers(
            resolverWrapper.getString("overworld", WorldIdentifiers.OVERWORLD_DEFAULT),
            resolverWrapper.getString("nether", WorldIdentifiers.NETHER_DEFAULT),
            resolverWrapper.getString("end", WorldIdentifiers.END_DEFAULT)
        )
    }

    override fun cache1_17Light(): Boolean {
        return resolverWrapper.getBoolean("cache-1_17-light", true)
    }

    override fun isArmorToggleFix(): Boolean {
        return false
    }

    override fun translateOcelotToCat(): Boolean {
        return resolverWrapper.getBoolean("translate-ocelot-to-cat", true)
    }

    override fun enforceSecureChat(): Boolean {
        return resolverWrapper.getBoolean("enforce-secure-chat", false)
    }

    override fun handleInvalidItemCount(): Boolean {
        return resolverWrapper.getBoolean("handle-invalid-item-count", false)
    }

    override fun cancelBlockSounds(): Boolean {
        return resolverWrapper.getBoolean("cancel-block-sounds", true)
    }

    override fun hideScoreboardNumbers(): Boolean {
        return resolverWrapper.getBoolean("hide-scoreboard-numbers", false)
    }

    override fun fix1_21PlacementRotation(): Boolean {
        return resolverWrapper.getBoolean("fix-1_21-placement-rotation", true)
    }

    override fun cancelSwingInInventory(): Boolean {
        return resolverWrapper.getBoolean("cancel-swing-in-inventory", true)
    }

    // --- Управление ---
    override fun setCheckForUpdates(checkForUpdates: Boolean) = Unit
    override fun reload() = Unit
    override fun save() = Unit
    override fun set(path: String, value: Any) = Unit
    override fun getValues(): Map<String, Any> = emptyMap()
}
