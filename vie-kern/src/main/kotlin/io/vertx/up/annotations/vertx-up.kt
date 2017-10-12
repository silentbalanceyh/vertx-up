package io.vertx.up.annotations

/**
 * Worker thread
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Acteur(
        val value: Opts
)

/**
 * Agent thread
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Agent(
        val value: Opts
)
