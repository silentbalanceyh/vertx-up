package io.vertx.up.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Opts(
        // JsonFile path from class path
        val jsonFile: String
)
