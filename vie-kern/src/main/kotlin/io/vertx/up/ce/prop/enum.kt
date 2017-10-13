package io.vertx.up.ce.prop

import io.vertx.up.hors.ArgumentsException
import java.util.*

inline fun <reified T : Any> enumType(allowed: Map<String, T>) = enumType(T::class.java, allowed)

fun <T : Any> enumType(enumType: Class<T>, allowed: Map<String, T>) = propType(enumType) { input ->
    allowed[input]
            ?.let { Result.Success(it) }
            ?: Result.Failure<T>(ArgumentsException("Invalid value: $input; must be one of: ${allowed.keys}").to())
}

fun <T : Enum<T>> enumType(enumClass: Class<T>, allowed: Iterable<T>) = enumType(enumClass, allowed.associate { it.name to it })

inline fun <reified T : Enum<T>> enumType(allowed: Iterable<T>) = enumType(T::class.java, allowed.associate { it.name to it })

inline fun <reified T : Any> enumType(vararg allowed: Pair<String, T>) = enumType(mapOf(*allowed))

inline fun <reified T : Enum<T>> enumType(vararg allowed: T) = enumType(listOf(*allowed))

fun <T : Enum<T>> enumType(enumClass: java.lang.Class<T>) = enumType(enumClass, EnumSet.allOf(enumClass))

inline fun <reified T : Enum<T>> enumType() = enumType(T::class.java)

