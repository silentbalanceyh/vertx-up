package io.vertx.up.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Agent(
        // Single properties to refer to Opts
        val value: Opts
)
