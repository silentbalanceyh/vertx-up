package io.vertx.up.resource

import io.vertx.up.ce.Key
import io.vertx.up.ce.Path
import io.vertx.up.ce.PropertyPath

/**
 * Inceptor to read data from configuration files.
 */
interface Inceptor {

    fun <T> getOrNull(key: Key<T>): T?

    fun <T> getOrElse(key: Key<T>, default: T): T = getOrElse(key) { default }

    fun <T> getOrElse(key: Key<T>, default: (Key<T>) -> T): T = getOrNull(key) ?: default(key)

    fun contains(key: Key<*>) = null != getOrNull(key)

    fun search(key: Key<*>): List<PropertyPath>

    fun list(): List<Pair<Path, Map<String, String>>>
}
