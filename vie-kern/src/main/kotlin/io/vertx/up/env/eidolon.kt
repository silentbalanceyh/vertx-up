package io.vertx.up.env

import io.vertx.up.ce.Key
import io.vertx.up.ce.Path
import io.vertx.up.ce.PropertyPath
import io.vertx.up.cv.Un
import io.vertx.up.hors.MissingKeyException
import java.io.File
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * Lookup override things
 */
class DataOverride(
        val override: Datum,
        val fallback: Datum
) : Datum {
    override fun search(key: Key<*>) = override.search(key) + fallback.search(key)

    override fun <T> getOrNull(key: Key<T>) = override.getOrNull(key) ?: fallback.getOrNull(key)

    override fun list() = override.list() + fallback.list()
}

/**
 * Empty Storage
 */
class DataEmpty : Datum {
    override fun <T> get(key: Key<T>) = throw MissingKeyException(missingKey(key))
    override fun <T> getOrElse(key: Key<T>, default: T) = default
    override fun <T> getOrElse(key: Key<T>, default: (Key<T>) -> T) = default(key)
    override fun <T> getOrNull(key: Key<T>) = null
    override fun contains(key: Key<*>) = false
    override fun list() = emptyList<Pair<Path, Map<String, String>>>()
    override fun search(key: Key<*>) = emptyList<PropertyPath>()
}

/**
 * Map Storage
 */
class DataMap(
        private val properties: Map<String, String>,
        override val path: Path = Path.UP
) : DataBase() {

    override fun <T> getOrNull(key: Key<T>) = key.getOrNullBy { location(key) to properties[key.name] }

    override fun contains(key: Key<*>): Boolean {
        return key.name in properties
    }

    override fun list(): List<Pair<Path, Map<String, String>>> {
        return listOf(path to properties)
    }
}

/**
 * Environment Variable Storage
 */
class DataEnvironment(
        val prefix: String = Un.EMPTY,
        private val lookup: (String) -> String? = System::getenv,
        private val all: () -> Map<String, String> = System::getenv
) : Datum {
    val path = Path("environment variables")

    override fun <T> getOrNull(key: Key<T>) = key.getOrNullBy { name ->
        val env = toEnvUnit(name)
        PropertyPath(key, path, env) to lookup(env)
    }

    override fun search(key: Key<*>) =
            listOf(PropertyPath(key, path, toEnvUnit(key.name)))

    override fun list(): List<Pair<Path, Map<String, String>>> =
            listOf(path to all().filterKeys { it.startsWith(prefix) })

    private fun toEnvUnit(name: String) =
            prefix + name.toUpperCase().replace("[^A-Za-z0-9]", Un.UNDERLINE)

    companion object : Datum by DataEnvironment
}

/**
 * Properties Storage
 */
class DataProperty(
        private val properties: Properties,
        override val path: Path = Path.UP
) : DataBase() {
    override fun <T> getOrNull(key: Key<T>) = key.getOrNullBy { name ->
        PropertyPath(key, path, name) to properties.getProperty(name)
    }

    override fun contains(key: Key<*>) = null != properties.getProperty(key.name)

    override fun search(key: Key<*>): List<PropertyPath> {
        return listOf(PropertyPath(key, path, key.name))
    }

    override fun list(): List<Pair<Path, Map<String, String>>> {
        return listOf(path to properties.stringPropertyNames().associateBy({ it }, { properties.getProperty(it) }))
    }

    companion object {
        /**
         * Return the system properties
         */
        @JvmStatic
        fun system() = DataProperty(System.getProperties(), Path("system properties"))

        /**
         * Load from resource relative to a class
         */
        @JvmStatic
        fun resource(relativeCls: Class<*>, name: String) =
                load(name, relativeCls.getResource(name))

        /**
         * Load from resource within the system class loader.
         */
        @JvmStatic
        fun resource(name: String): DataProperty {
            val loader = ClassLoader.getSystemClassLoader()
            return load(name, loader.getResource(name))
        }

        /**
         * Read data from file
         */
        @JvmStatic
        fun file(file: File) = load(
                if (file.exists()) file.inputStream() else null,
                Path(file.absolutePath, file.toURI())
        ) { "[Env] File $file does not exist." }

        // -------------- Private Loading ----------------------
        private fun load(name: String, url: URL?): DataProperty {
            return load(url?.openStream(), Path("Resource $name", url?.toURI())) {
                "[Env] Resource $name not found."
            }
        }

        private fun load(input: InputStream?, path: Path, errorFn: () -> String) = (input ?: throw MissingKeyException(errorFn())).use {
            DataProperty(Properties().apply { load(input) }, path)
        }
    }
}

/**
 * Child a layer set of configuration properties.
 * For example [namePrefix] = "db", other properties should be picked up by
 * db.username, db.password etc.
 */
class DataChild(
        namePrefix: String,
        private val datum: Datum
) : Datum {

    private val prefix = namePrefix + Un.DOT

    override fun <T> getOrNull(key: Key<T>) = datum.getOrNull(prefixed(key))

    override fun contains(key: Key<*>) = datum.contains(prefixed(key))

    override fun search(key: Key<*>) = datum.search(prefixed(key))

    override fun list() = datum.list().map { it.first to it.second.filterKeys { key -> key.startsWith(prefix) } }

    private fun <T> prefixed(key: Key<T>) = key.copy(name = prefix + key.name)
}
