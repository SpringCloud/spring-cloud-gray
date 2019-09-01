package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.event.GrayEventPublisher;
import cn.springcloud.gray.event.GraySourceEventPublisher;
import cn.springcloud.gray.server.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class GrayServerEventAutoConfiguration {





    @Bean
    @ConditionalOnMissingBean
    public GrayEventPublisher grayEventPublisher() {
        return new DefaultGrayEventPublisher();
    }


    @Bean
    @ConditionalOnMissingBean
    public EventSourceConvertService eventSourceConvertService(List<EventSourceConverter> converters){
        return new EventSourceConvertService.Default(converters);
    }


    @Bean
    @ConditionalOnMissingBean
    public GraySourceEventPublisher graySourceEventPublisher(
            GrayEventPublisher publisherDelegater,
            @Autowired(required = false) ExecutorService grayEventExecutorService,
            EventSourceConvertService eventSourceConvertService){
        if(grayEventExecutorService==null){
            grayEventExecutorService = new ThreadPoolExecutor(5, 20,
                    1l, TimeUnit.MINUTES, new ArrayBlockingQueue<>(40));
        }
        return new DefaultGraySourceEventPublisher(publisherDelegater, grayEventExecutorService, eventSourceConvertService);
    }



    @Configuration
    public static class EventSourceConvertConfiguration{


        @Bean
        public GrayDecisionEventSourceConverter grayDecisionEventSourceConverter(){
            return new GrayDecisionEventSourceConverter();
        }

        @Bean
        public GrayInstanceEventSourceConverter grayInstanceEventSourceConverter(){
            return new GrayInstanceEventSourceConverter();
        }

        @Bean
        public GrayPolicyEventSourceConverter grayPolicyEventSourceConverter(){
            return new GrayPolicyEventSourceConverter();
        }

        @Bean
        public GrayTrackEventSourceConverter grayTrackEventSourceConverter(){
            return new GrayTrackEventSourceConverter();
        }

    }




}
