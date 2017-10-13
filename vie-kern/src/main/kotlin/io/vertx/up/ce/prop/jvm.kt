@file:JvmName("Prop")

package io.vertx.up.ce.prop

import io.vertx.up.hors.VertxUpException
import java.net.URI
import java.net.URISyntaxException
import java.time.*


sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Failure<T>(val exception: VertxUpException) : Result<T>()
}

@JvmField
val stringType = propType { Result.Success(it) }

@JvmField
val intType = numericType(String::toInt)

@JvmField
val longType = numericType(String::toLong)

@JvmField
val doubleType = numericType(String::toDouble)

@JvmField
val booleanType = propType { Result.Success(it.toBoolean()) }

@JvmField
val uriType = propType(parser<URI, URISyntaxException>(::URI))

@JvmField
val durationType = temporalType(Duration::parse)

@JvmField
val periodType = temporalType(Period::parse)

@JvmField
val instantType = temporalType(Instant::parse)

// Uniform for local date time using, removed local prefix from each method.
@JvmField
val timeType = temporalType(LocalTime::parse)

@JvmField
val dateType = temporalType(LocalDate::parse)

@JvmField
val dateTimeType = temporalType(LocalDateTime::parse)
