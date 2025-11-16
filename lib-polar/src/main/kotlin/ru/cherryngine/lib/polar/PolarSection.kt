package ru.cherryngine.lib.polar

/**
 * Representation of the latest version of the section format.
 *
 *
 * Marked as internal because of the use of mutable arrays. These arrays must _not_ be mutated.
 * This class should be considered immutable.
 */
class PolarSection {
    enum class LightContent {
        MISSING, EMPTY, FULL, PRESENT;

        companion object {
            val VALUES: Array<LightContent> = entries.toTypedArray()
        }
    }

    val isEmpty: Boolean

    private val blockPalette: Array<String>
    private val blockData: IntArray?

    private val biomePalette: Array<String>
    private val biomeData: IntArray?

    private val blockLightContent: LightContent
    private val blockLight: ByteArray?
    private val skyLightContent: LightContent
    private val skyLight: ByteArray?

    constructor() {
        this.isEmpty = true

        this.blockPalette = arrayOf<String>("minecraft:air")
        this.blockData = null
        this.biomePalette = arrayOf<String>("minecraft:plains")
        this.biomeData = null

        this.blockLightContent = LightContent.MISSING
        this.blockLight = null
        this.skyLightContent = LightContent.MISSING
        this.skyLight = null
    }

    constructor(
        blockPalette: Array<String>, blockData: IntArray?,
        biomePalette: Array<String>, biomeData: IntArray?,
        blockLightContent: LightContent, blockLight: ByteArray?,
        skyLightContent: LightContent, skyLight: ByteArray?
    ) {
        this.isEmpty = false

        this.blockPalette = blockPalette
        this.blockData = blockData
        this.biomePalette = biomePalette
        this.biomeData = biomeData

        this.blockLightContent = blockLightContent
        this.blockLight = blockLight
        this.skyLightContent = skyLightContent
        this.skyLight = skyLight
    }

    fun blockPalette(): Array<String> {
        return blockPalette
    }

    /**
     * Returns the uncompressed palette data. Each int corresponds to an index in the palette.
     * Always has a length of 4096.
     */
    fun blockData(): IntArray? {
        return blockData
    }

    fun biomePalette(): Array<String> {
        return biomePalette
    }

    /**
     * Returns the uncompressed palette data. Each int corresponds to an index in the palette.
     * Always has a length of 256.
     */
    fun biomeData(): IntArray? {
        return biomeData
    }

    fun blockLightContent(): LightContent {
        return blockLightContent
    }

    fun blockLight(): ByteArray? {
        return blockLight
    }

    fun skyLightContent(): LightContent {
        return skyLightContent
    }

    fun skyLight(): ByteArray? {
        return skyLight
    }

    companion object {
        const val BLOCK_PALETTE_SIZE: Int = 4096
        const val BIOME_PALETTE_SIZE: Int = 64
    }
}
