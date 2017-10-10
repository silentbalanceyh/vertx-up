package io.vertx.up.ce

import java.io.File
import java.net.URI

data class Path(val path: String, val uri: URI? = null) {

    constructor(file: File) : this(file.absolutePath, file.toURI())

    constructor(uri: URI) : this(uri.toString(), uri)

    companion object {
        /**
         * Default config folder: src/main/resources/up/...
         */
        val UP = Path("up")
    }
}


data class Key<out T>(val name: String, val parse: (PropertyPath, String) -> T) {

    fun getOrNullBy(lookup: (String) -> Pair<PropertyPath, String?>): T? {
        val (location, value) = lookup(name)

        return value?.let { parse(location, value) }
    }
}

data class PropertyPath(val key: Key<*>, val source: Path, val name: String) {
    val description: String get() = "$name in ${source.path}"
}
