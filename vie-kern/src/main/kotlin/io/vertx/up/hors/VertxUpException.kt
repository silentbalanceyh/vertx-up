package io.vertx.up.hors

abstract class VertxUpException constructor(message: String) : Exception() {

    private val errorMessage = message

    open fun getErrorMessage(): String {
        return this.errorMessage
    }
}
