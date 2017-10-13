@file: JvmName("VertxUp")

package io.vertx.up.hors

/**
 * Checked Exception
 */
abstract class VertxUpException(message: String,
                                cause: Throwable? = null) :
        Exception() {
    // Set the message content for getMessage
    override val message = "[Vx UP] ${message}"

    open fun message(): String = this.message

    open fun to(): Throwable = cause as Throwable
}

/**
 * Runtime Exception
 */
abstract class VertxUpRunException(message: String,
                                   cause: VertxUpException? = null) :
        RuntimeException("[Vx Run] ${message}", cause) {

    open fun to(): VertxUpException = this.cause as VertxUpException
}
