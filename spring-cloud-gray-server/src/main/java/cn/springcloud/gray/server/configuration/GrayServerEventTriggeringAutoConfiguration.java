package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.event.triggering.GrayEventCodec;
import cn.springcloud.gray.server.event.triggering.GrayEventLogRetriever;
import cn.springcloud.gray.server.event.triggering.GrayEventStorage;
import cn.springcloud.gray.server.event.triggering.JsonGrayEventCodec;
import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springlcoud.gray.event.server.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-31 22:00
 */
@Configuration
public class GrayServerEventTriggeringAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public GrayEventSender grayEventSender() {
        return new GrayEventSender.Default();
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayEventCodec grayEventCodec(
            @Autowired(required = false) ObjectMapper grayEventObjectMapper) {
        if (Objects.isNull(grayEventObjectMapper)) {
            grayEventObjectMapper = new ObjectMapper();
        }
        return new JsonGrayEventCodec(grayEventObjectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayEventLogger grayEventLogger(GrayEventLogModule grayEventLogModule, GrayEventCodec grayEventCodec) {
        return new GrayEventStorage(grayEventLogModule, grayEventCodec);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayEventTrigger grayEventTrigger(
            GrayEventSender grayEventSender, List<EventConverter> eventConverters, GrayEventLogger grayEventLogger) {
        return new DefaultGrayEventTrigger(grayEventSender, eventConverters, grayEventLogger);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayEventRetriever grayEventRetriever(
            GrayEventLogModule grayEventLogModule,
            List<EventConverter> eventConverters,
            GrayEventCodec grayEventCodec) {
        return new GrayEventLogRetriever(grayEventLogModule, eventConverters, grayEventCodec);
    }


}
