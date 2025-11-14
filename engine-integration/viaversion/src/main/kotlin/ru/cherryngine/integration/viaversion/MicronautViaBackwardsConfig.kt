package ru.cherryngine.integration.viaversion

import com.viaversion.viabackwards.api.DialogStyleConfig
import com.viaversion.viabackwards.api.ViaBackwardsConfig
import io.micronaut.core.value.PropertyResolver
import jakarta.inject.Singleton

@Singleton
class MicronautViaBackwardsConfig(
    propertyResolver: PropertyResolver,
) : ViaBackwardsConfig {
    private val resolverWrapper = PropertyResolverWrapper(propertyResolver, "viabackwards")

    private fun PropertyResolverWrapper.loadDialogStyleConfig(): DialogStyleConfig {
        return DialogStyleConfig(
            getString("page-navigation-title", "&9&lPage navigation"),
            getString("page-navigation-next", "&9Left click: &6Go to next page"),
            getString("page-navigation-previous", "&9Right click: &6Go to previous page"),
            getString("increase-value", "&9Left click: &6Increase value by %s"),
            getString("decrease-value", "&9Right click: &6Decrease value by %s"),
            getString("value-range", "&7(Value between &a%s &7and &a%s&7)"),
            getString("next-option", "&9Left click: &6Go to next option"),
            getString("previous-option", "&9Right click: &6Go to previous option"),
            getString("current-value", "&7Current value: &a%s"),
            getString("edit-value", "&9Left click: &6Edit text"),
            getString("set-text", "&9Left click/close: &6Set text"),
            getString("close", "&9Left click: &6Close"),
            getString("toggle-value", "&9Left click: &6Toggle value")
        )
    }

    // --- Базовые поля ---
    override fun addCustomEnchantsToLore(): Boolean {
        return resolverWrapper.getBoolean("add-custom-enchants-into-lore", true)
    }

    override fun addTeamColorTo1_13Prefix(): Boolean {
        return resolverWrapper.getBoolean("add-teamcolor-to-prefix", true)
    }

    override fun isFix1_13FacePlayer(): Boolean {
        return resolverWrapper.getBoolean("fix-1_13-face-player", false)
    }

    override fun fix1_13FormattedInventoryTitle(): Boolean {
        return resolverWrapper.getBoolean("fix-formatted-inventory-titles", true)
    }

    override fun alwaysShowOriginalMobName(): Boolean {
        return resolverWrapper.getBoolean("always-show-original-mob-name", true)
    }

    override fun handlePingsAsInvAcknowledgements(): Boolean {
        return resolverWrapper.getBoolean("handle-pings-as-inv-acknowledgements", false)
    }

    override fun bedrockAtY0(): Boolean {
        return resolverWrapper.getBoolean("bedrock-at-y-0", false)
    }

    override fun sculkShriekerToCryingObsidian(): Boolean {
        return resolverWrapper.getBoolean("sculk-shriekers-to-crying-obsidian", false)
    }

    override fun scaffoldingToWater(): Boolean {
        return resolverWrapper.getBoolean("scaffolding-to-water", false)
    }

    override fun mapDarknessEffect(): Boolean {
        return resolverWrapper.getBoolean("map-darkness-effect", true)
    }

    override fun mapCustomModelData(): Boolean {
        return resolverWrapper.getBoolean("map-custom-model-data", true)
    }

    override fun mapDisplayEntities(): Boolean {
        return resolverWrapper.getBoolean("map-display-entities", true)
    }

    override fun suppressEmulationWarnings(): Boolean {
        return resolverWrapper.getBoolean("suppress-emulation-warnings", false)
    }

    override fun dialogStyleConfig(): DialogStyleConfig {
        return resolverWrapper.section("dialog-style").loadDialogStyleConfig()
    }

    // --- Управление ---
    override fun reload() = Unit
    override fun save() = Unit
    override fun set(path: String, value: Any) = Unit
    override fun getValues(): Map<String, Any> = emptyMap()
}
