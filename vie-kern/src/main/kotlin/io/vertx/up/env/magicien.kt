package io.vertx.up.env

import io.vertx.up.ce.Key
import io.vertx.up.ce.PropertyPath
import io.vertx.up.hors.ArgumentsException
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

open class DatumKeys : Iterable<Key<*>> {
    override operator fun iterator(): Iterator<Key<*>> {
        val key = javaClass.kotlin
        return (key.memberProperties.map { it.get(this) }.filterIsInstance<Key<*>>() +
                key.nestedClasses.map { it.objectInstance }.filterIsInstance<DatumPaquet>().flatten()).iterator()
    }
}

/**
 * Group of DatumKeys
 */
open class DatumPaquet(private val outer: DatumPaquet? = null) : DatumKeys() {
    private fun outer() = outer ?: javaClass.enclosingClass?.kotlin?.objectInstance as? DatumPaquet
    private fun name(): String = prefix() + group()
    private fun prefix() = outer()?.name()?.let { it + "." } ?: ""
    private fun group() = javaClass.kotlin.simpleName?.substringBefore("$") ?:
            throw ArgumentsException("[Env] Cannot determine name of property paquet(group)")

    fun <T> key(name: String, type: (PropertyPath, String) -> T): Key<T> {
        return key((name() + "." + name).replace('_', '-'), type)
    }
}

/**
 * Override operators.
 */
operator fun <P : DatumPaquet, T> ((PropertyPath, String) -> T).getValue(group: P, property: KProperty<*>)
        = group.key(property.name, this)

operator fun <SCOPE, T> ((PropertyPath, String) -> T).getValue(scope: SCOPE?, property: KProperty<*>)
        = Key(property.name, this)
