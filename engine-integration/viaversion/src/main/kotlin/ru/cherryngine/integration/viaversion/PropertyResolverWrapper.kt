package ru.cherryngine.integration.viaversion

import io.micronaut.core.convert.ConversionContext
import io.micronaut.core.value.PropertyResolver

class PropertyResolverWrapper(
    private val propertyResolver: PropertyResolver,
    private val prefix: String
) {
    fun getBoolean(key: String, default: Boolean): Boolean {
        return propertyResolver.getProperty("$prefix.$key", Boolean::class.java)
            .orElse(default)
    }

    fun getInt(key: String, default: Int): Int {
        return propertyResolver.getProperty("$prefix.$key", Int::class.java)
            .orElse(default)
    }

    fun getDouble(key: String, default: Double): Double {
        return propertyResolver.getProperty("$prefix.$key", Double::class.java)
            .orElse(default)
    }

    fun getString(key: String, default: String): String {
        return propertyResolver.getProperty("$prefix.$key", String::class.java)
            .orElse(default)
    }

    fun getStringList(key: String, default: List<String>): List<String> {
        return propertyResolver.getProperty("$prefix.$key", ConversionContext.LIST_OF_STRING)
            .orElse(default)
    }

    fun section(sectionPrefix: String): PropertyResolverWrapper {
        return PropertyResolverWrapper(propertyResolver, "$prefix.$sectionPrefix")
    }
}