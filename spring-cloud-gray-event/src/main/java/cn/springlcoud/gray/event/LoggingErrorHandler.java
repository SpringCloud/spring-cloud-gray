package cn.springlcoud.gray.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author saleson
 * @date 2020-01-30 15:26
 */
public class LoggingErrorHandler implements ErrorHandler {

    private Logger log;

    public LoggingErrorHandler() {
        this(LoggingErrorHandler.class);
    }

    public LoggingErrorHandler(Class logCls) {
        this.log = LoggerFactory.getLogger(logCls);
    }


    @Override
    public void handleError(Throwable t) {

    }
}
