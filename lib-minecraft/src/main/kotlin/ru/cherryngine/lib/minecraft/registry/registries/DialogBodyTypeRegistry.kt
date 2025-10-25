package ru.cherryngine.lib.minecraft.registry.registries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.dialog.body.DialogBody
import ru.cherryngine.lib.minecraft.dialog.body.DialogItemBody
import ru.cherryngine.lib.minecraft.dialog.body.PlainMessage
import ru.cherryngine.lib.minecraft.protocol.packets.configurations.ClientboundRegistryDataPacket
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import kotlin.reflect.KClass

object DialogBodyTypeRegistry : DynamicRegistry<DialogBodyType>() {
    override val identifier: String = "minecraft:dialog_body_type"

    init {
        addEntry(DialogBodyType("minecraft:item", DialogItemBody::class))
        addEntry(DialogBodyType("minecraft:plain_message", PlainMessage::class))
    }

    override fun updateCache() {
        cachedPacket = ClientboundRegistryDataPacket(this)
    }
}

data class DialogBodyType(
    val identifier: String,
    val clazz: KClass<out DialogBody>
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag? {
        return null
    }

    override fun getProtocolId(): Int {
        return DialogBodyTypeRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}