package io.vertx.up.env

import io.vertx.up.ce.Key
import io.vertx.up.ce.Path
import io.vertx.up.ce.PropertyPath
import io.vertx.up.hors.MissingKeyException

/**
 * Abstract interface to read data from configuration files.
 */
interface Datum {

    @Throws(MissingKeyException::class)
    operator fun <T> get(key: Key<T>): T = getOrElse(key) { throw MissingKeyException(missingKey(it)) }

    fun <T> getOrNull(key: Key<T>): T?

    fun <T> getOrElse(key: Key<T>, default: T): T = getOrElse(key) { default }

    fun <T> getOrElse(key: Key<T>, default: (Key<T>) -> T): T = getOrNull(key) ?: default(key)

    fun contains(key: Key<*>) = null != getOrNull(key)

    fun search(key: Key<*>): List<PropertyPath>

    fun list(): List<Pair<Path, Map<String, String>>>

    /**
     * Message for debugging, defined for Inceptor
     */
    val List<PropertyPath>.description: String
        get() = map { " - ${it.description}" }.joinToString(separator = "\n", postfix = "\n")

    fun Datum.missingKey(key: Key<*>) =
            "[Env] ${key.name} property missing; searched: \n${search(key).description}"
}

/**
 * Abstract parent class for configuration
 */
abstract class DataBase : Datum {

    abstract val path: Path

    override fun search(key: Key<*>): List<PropertyPath> = listOf(location(key))

    protected fun location(key: Key<*>) = PropertyPath(key, path, key.name)
}

/**
 * Public function defined for future using
 */
@JvmName("DataMapFromNames")
fun DataMap(vararg enties: Pair<String, String>, path: Path = Path.UP) =
        DataMap(enties.toMap(), path)

@JvmName("DataMapFromKeys")
fun DataMap(vararg enties: Pair<Key<*>, String>, path: Path = Path.UP) =
        DataMap(enties.map { (key, value) -> key.name to value }.toMap(), path)

/**
 * Look up configuration in [override] and if not defined, looks it up in [fallback]
 */
infix fun Datum.overriding(defaults: Datum?) = if (null == defaults) this else DataOverride(this, defaults)

fun Hunt(first: Datum, vararg rest: Datum) = rest.fold(first, ::DataOverride)
