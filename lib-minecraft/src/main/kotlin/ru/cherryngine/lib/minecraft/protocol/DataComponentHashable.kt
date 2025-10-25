package ru.cherryngine.lib.minecraft.protocol

import ru.cherryngine.lib.minecraft.data.HashHolder
import kotlin.reflect.KClass

interface DataComponentHashable {
    @Deprecated("Please use CODEC and CRC32CTranscoder", replaceWith = ReplaceWith("CRC32Transcoder"))
    fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    fun unsupported(kclass: KClass<*>): HashHolder {
        throw kotlin.UnsupportedOperationException("${kclass.simpleName} is not supported yet! If you require use of this component please open an issue")
    }

    fun unsupported(any: Any): HashHolder {
        return unsupported(any::class)
    }
}