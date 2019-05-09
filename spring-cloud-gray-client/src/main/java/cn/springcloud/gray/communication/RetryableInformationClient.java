package cn.springcloud.gray.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RetryableInformationClient继承了InformationClientDecorator类，实现了重试的功能。
 */
public class RetryableInformationClient extends InformationClientDecorator {
    private static final Logger log = LoggerFactory.getLogger(RetryableInformationClient.class);


    public static final int DEFAULT_NUMBER_OF_RETRIES = 3;

    private final int numberOfRetries;

    private InformationClient delegate;


    public RetryableInformationClient(int numberOfRetries, InformationClient delegate) {
        this.numberOfRetries = numberOfRetries;
        this.delegate = delegate;
    }

    @Override
    protected <R> R execute(RequestExecutor<R> requestExecutor) {
        for (int retry = 0; retry < numberOfRetries; retry++) {
            try {
                R retval = requestExecutor.execute(delegate);
                if (retry > 0) {
                    log.info("Request execution succeeded on retry #{}", retry);
                }
                return retval;
            } catch (Exception e) {
                log.warn("Request execution failed with message: {}", e.getMessage());  // just log message as the underlying client should log the stacktrace
            }
        }

        throw new RuntimeException("Retry limit reached; giving up on completing the request");
    }
}
