package ru.cherryngine.lib.minecraft.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.EitherCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.TypedMapCodec
import ru.cherryngine.lib.minecraft.utils.Either
import java.util.concurrent.ConcurrentHashMap

class EnvironmentAttributeMap(
    val entries: Map<EnvironmentAttribute<*>, Entry<*, *>>,
) {
    companion object {
        val EMPTY = EnvironmentAttributeMap(mapOf())

        val CODEC = TypedMapCodec(
            EnvironmentAttribute.CODEC,
            { Entry.codec0(it) },
            Int.MAX_VALUE,
            ConcurrentHashMap()
        ).transform(::EnvironmentAttributeMap, EnvironmentAttributeMap::entries)
    }

    data class Entry<T, Arg>(
        val argument: Arg,
        val modifier: Modifier<T, Arg>,
    ) {
        companion object {
            @Suppress("UNCHECKED_CAST")
            fun <T> codec(attribute: EnvironmentAttribute<T>): Codec<Entry<T, *>> {
                // A value is represented by either a single value which acts as an override,
                // or a struct with `modifier` and `argument` keys (full codec).

                val fullCodec: Codec<Entry<T, *>> = attribute.type.modifierCodec
                    .union(
                        "modifier",
                        attribute.type.modifierCodec,
                        { modifier -> fullCodec(modifier) },
                        { it.modifier }
                    )

                val override = Modifier.Override(attribute.type.codec)
                return EitherCodec(attribute.type.codec, fullCodec).transform(
                    { either ->
                        either.unify(
                            { value -> Entry(value, override) },
                            { u -> u }
                        )
                    },
                    { entry ->
                        if (entry.modifier is Modifier.Override<*>)
                            Either.Left(entry.argument as T)
                        else
                            Either.Right(entry)
                    })
            }

            @Suppress("UNCHECKED_CAST")
            fun codec0(attribute: EnvironmentAttribute<*>): Codec<Entry<*, *>> {
                return codec(attribute) as Codec<Entry<*, *>>
            }

            private fun <T, Arg> fullCodec(modifier: Modifier<T, Arg>): StructCodec<Entry<T, Arg>> {
                return StructCodec.of(
                    "argument", modifier.argumentCodec(), { it.argument },
                    { argument -> Entry(argument, modifier) }
                )
            }
        }
    }
}