package ru.cherryngine.lib.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonSerializationProcessor

@Factory
class JacksonModule {
    @Singleton
    fun jsonMapper(serializers: List<JacksonSerializationProcessor<*>>): JsonMapper {
        return JsonMapper.builder()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .addModule(kotlinModule())
            .addModule(serializers.module())
            .build()
    }

    @Singleton
    fun yamlMapper(serializers: List<JacksonSerializationProcessor<*>>): YAMLMapper {
        val yamlMapper = YAMLMapper.builder()
            .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
            .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(kotlinModule())
            .addModule(serializers.module())
            .build()
        return CommentsYamlMapper(yamlMapper)
    }
}