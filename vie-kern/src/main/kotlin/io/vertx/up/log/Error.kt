package io.vertx.up.log

import io.vertx.up.cv.Constants
import io.vertx.up.hors.VertxUpException
import java.text.MessageFormat

/**
 * Specifical Error
 */
object Error {

    fun vm(clazz: Class<Any>, exp: Throwable) {
        val logger = Annal.get(clazz)
        logger.error(MessageFormat.format(Constants.Log.MSG_VM, exp.message))
    }

    fun up(clazz: Class<Any>, exp: VertxUpException) {

    }

    fun error(clazz: Class<Any>, code: Int, vararg params: Any) {
        
    }
}
