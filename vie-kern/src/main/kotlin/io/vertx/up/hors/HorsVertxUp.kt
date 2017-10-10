package io.vertx.up.hors

/**
 * Checked Exception
 */
abstract class VertxUpException(message: String) :
        Exception() {

    private val errorMessage = message

    open fun getErrorMessage(): String {
        return this.errorMessage
    }
}

/**
 * Runtime Exception
 */
abstract class VertxUpRunException(message: String,
                                   cause: VertxUpException? = null) :
        RuntimeException(message, cause) {
}
