package io.vertx.up.util

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.up.cv.Log
import io.vertx.up.hors.VertxUpException
import java.text.MessageFormat

/**
 * Uniform logging interfaces.
 */
object Annal {

    @JvmStatic
    fun info(clazz: Class<Any>, pattern: String, vararg args: Any) {
        val logger = get(clazz)
        this.execute(logger::isInfoEnabled, logger::info, pattern, args)
    }

    @JvmStatic
    fun debug(clazz: Class<Any>, pattern: String, vararg args: Any) {
        val logger = get(clazz)
        this.execute(logger::isDebugEnabled, logger::debug, pattern, args)
    }

    @JvmStatic
    fun trace(clazz: Class<Any>, pattern: String, vararg args: Any) {
        val logger = get(clazz)
        this.execute(logger::isTraceEnabled, logger::trace, pattern, args)
    }

    @JvmStatic
    fun warn(clazz: Class<Any>, pattern: String, vararg args: Any) {
        this.execute(get(clazz)::warn, pattern, args)
    }

    @JvmStatic
    fun error(clazz: Class<Any>, pattern: String, vararg args: Any) {
        this.execute(get(clazz)::error, pattern, args)
    }

    @JvmStatic
    fun fatal(clazz: Class<Any>, pattern: String, vararg args: Any) {
        this.execute(get(clazz)::fatal, pattern, args)
    }

    @JvmStatic
    fun get(clazz: Class<Any>): Logger {
        return LoggerFactory.getLogger(clazz)
    }

    inline private fun execute(log: (message: Any) -> Unit,
                               pattern: String, vararg args: Any) {
        val message = MessageFormat.format(pattern, args);
        log(message);
    }

    inline private fun execute(enabled: () -> Boolean,
                               log: (message: Any) -> Unit,
                               pattern: String, vararg args: Any) {
        if (enabled()) {
            this.execute(log, pattern, args)
        }
    }
}

/**
 * Common error output for three categories
 * 1. JVM error
 * 2. Container error
 * 3. System error message generation
 */
object Error {

    fun vm(clazz: Class<Any>, exp: Throwable) {
        val logger = Annal.get(clazz)
        logger.error(MessageFormat.format(Log.MSG_VM, exp.message))
    }

    fun up(clazz: Class<Any>, exp: VertxUpException) {

    }

    fun error(clazz: Class<Any>, code: Int, vararg params: Any) {

    }
}
