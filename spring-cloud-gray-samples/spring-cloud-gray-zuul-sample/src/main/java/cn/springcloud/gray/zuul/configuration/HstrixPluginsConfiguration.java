package cn.springcloud.gray.zuul.configuration;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

//@Configuration
public class HstrixPluginsConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HstrixPluginsConfiguration.class);


    @PostConstruct
    public void init() {
        HystrixPlugins.getInstance().registerEventNotifier(new HystrixEventNotifier() {
            @Override
            public void markEvent(HystrixEventType eventType, HystrixCommandKey key) {
                super.markEvent(eventType, key);
                String serviceId = key.name();
                log.info("service id:{}, key:{}, event type:{}",
                        serviceId,
                        key.name(),
                        eventType.name());
            }
        });


        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCommandExecutionHook() {
            @Override
            public <T> T onEmit(HystrixInvokable<T> commandInstance, T value) {
                log.info("onEmit commandInstance:{}, value:{}", commandInstance, value);
                return super.onEmit(commandInstance, value);
            }

            @Override
            public <T> T onExecutionEmit(HystrixInvokable<T> commandInstance, T value) {
                log.info("onExecutionEmit commandInstance:{}, value:{}", commandInstance, value);
                return super.onExecutionEmit(commandInstance, value);
            }

            @Override
            public <T> T onFallbackEmit(HystrixInvokable<T> commandInstance, T value) {
                log.info("onFallbackEmit commandInstance:{}, value:{}", commandInstance, value);
                return super.onFallbackEmit(commandInstance, value);
            }

            @Override
            public <T> void onStart(HystrixInvokable<T> commandInstance) {
                super.onStart(commandInstance);
            }
        });
    }


}
