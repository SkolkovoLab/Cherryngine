package ru.cherryngine.lib.kotlinx

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

@Factory
class KotlinxSerializationModule {
    @Singleton
    fun json(serializers: List<KSerializer<*>>): Json {
        val module = serializers.module()
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            this.serializersModule = module
        }
    }

    @Singleton
    fun yaml(serializers: List<KSerializer<*>>): Yaml {
        val module = serializers.module()
        return Yaml(
            configuration = YamlConfiguration(
                encodeDefaults = true,
                strictMode = false
            ),
            serializersModule = module
        )
    }
}