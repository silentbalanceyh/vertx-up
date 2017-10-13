@file:JvmName("Element")

package io.vertx.up.ce.prop

import io.vertx.up.ce.PropertyPath
import io.vertx.up.hors.MissingKeyException
import io.vertx.up.hors.VertxUpException
import java.time.format.DateTimeParseException

fun <T> propType(typeName: String, parse: (String) -> Result<T>): (PropertyPath, String) -> T {
    return { path, input ->
        val parsed = parse(input)
        when (parsed) {
            is Result.Success<T> -> parsed.value
            is Result.Failure<T> -> throw MissingKeyException(
                    "${path.source.path} ${path.name} - invalid $typeName: $input",
                    parsed.exception)
        }
    }
}

fun <T> propType(type: Class<T>, parse: (String) -> Result<T>) = propType(type.simpleName, parse)

inline fun <reified T : Any> propType(noinline parse: (String) -> Result<T>) = propType(T::class.java, parse)

fun <T, X : Throwable> parser(errorType: Class<X>, parse: (String) -> T) = fun(input: String) =
        try {
            Result.Success(parse(input))
        } catch (ex: VertxUpException) {
            if (errorType.isInstance(ex)) {
                Result.Failure<T>(ex)
            } else {
                throw ex
            }
        }

inline fun <T, reified X : Throwable> parser(noinline parse: (String) -> T) =
        parser(X::class.java, parse)

inline fun <reified T : Any> numericType(noinline parse: (String) -> T) =
        propType(parser<T, NumberFormatException>(parse))

inline fun <reified T : Any> temporalType(noinline fn: (String) -> T) = propType(parser<T, DateTimeParseException>(fn))

fun <T> listType(elementType: (PropertyPath, String) -> T, sperator: Regex = Regex(",\\s*")) {
    { path: PropertyPath, value: String ->
        value.split(sperator).map { element -> elementType(path, element) }
    }
}
