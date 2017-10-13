@file: JvmName("VertxUp")

package io.vertx.up.hors

/**
 * Checked Exception
 */
abstract class VertxUpException(message: String) :
        Exception() {
    // Set the message content for getMessage
    override val message = "[Vx UP] ${message}"

    open fun getErrorMessage(): String {
        return this.message
    }
}

/**
 * Runtime Exception
 */
abstract class VertxUpRunException(message: String,
                                   cause: VertxUpException? = null) :
        RuntimeException("[Vx Run] ${message}", cause) {

    open fun to(): VertxUpException {
        return this.cause as VertxUpException
    }
}
