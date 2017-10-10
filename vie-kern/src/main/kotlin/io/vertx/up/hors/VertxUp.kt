package io.vertx.up.hors

/**
 * Checked Exception
 */
abstract class VertxUpException(message: String) :
        Exception() {
    // Set the message content for getMessage
    override val message = message

    open fun getErrorMessage(): String {
        return this.message
    }
}

/**
 * Runtime Exception
 */
abstract class VertxUpRunException(message: String,
                                   cause: VertxUpException? = null) :
        RuntimeException(message, cause) {
}
