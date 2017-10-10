package io.vertx.up.resource

import io.vertx.up.ce.Key

/**
 * Inceptor to read data from configuration files.
 */
interface Inceptor {

    fun <T> getOrNull(key: Key<T>): T?

}
