package io.vertx.up.log

import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import java.text.MessageFormat

/**
 * Sample Logger
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
