package io.github.dockyardmc.protocol.types.predicate

import io.github.dockyardmc.tide.stream.MapStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.world.block.Block
import java.util.function.Predicate

class PropertiesPredicate(
    val properties: Map<String, ValuePredicate>
) : Predicate<Block> {
    override fun test(block: Block): Boolean {
        properties.entries.forEach { entry ->
            val value = block.blockStates[entry.key]
            if (!entry.value.test(value)) return false
        }
        return true
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(StreamCodec.STRING, ValuePredicate.STREAM_CODEC), PropertiesPredicate::properties,
            ::PropertiesPredicate
        )
    }
}



