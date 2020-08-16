package cn.springcloud.gray.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    public static Logger logger(Class<?> cls) {
        return LoggerFactory.getLogger(cls);
    }


    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    public static void log(LogLevel logLevel, String format, Object... arguments) {
        switch (logLevel) {
            case TRACE:
                trace(format, arguments);
                break;
            case DEBUG:
                debug(format, arguments);
                break;
            case INFO:
                info(format, arguments);
                break;
            case WARN:
                warn(format, arguments);
                break;
            case ERROR:
                error(format, arguments);
                break;
        }
    }

    public static void trace(String format, Object... arguments) {
        log.trace(format, arguments);
    }

    public static void debug(String format, Object... arguments) {
        log.debug(format, arguments);
    }

    public static void info(String format, Object... arguments) {
        log.info(format, arguments);
    }

    public static void warn(String format, Object... arguments) {
        log.warn(format, arguments);
    }

    public static void error(String format, Object... arguments) {
        log.error(format, arguments);
    }


    public enum LogLevel {

        TRACE, DEBUG, INFO, WARN, ERROR, OFF

    }
}
