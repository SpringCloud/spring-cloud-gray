package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.event.triggering.GrayEventInitializer;
import cn.springcloud.gray.server.event.triggering.GrayEventLogRetriever;
import cn.springcloud.gray.server.event.triggering.GrayEventStorage;
import cn.springcloud.gray.server.event.triggering.converter.*;
import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springlcoud.gray.event.codec.GrayEventCodec;
import cn.springlcoud.gray.event.codec.JsonGrayEventCodec;
import cn.springlcoud.gray.event.server.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
            grayEventObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
            GrayEventSender grayEventSender, GrayEventLogger grayEventLogger) {
        return new DefaultGrayEventTrigger(grayEventSender, grayEventLogger);
    }


    @Bean
    @ConditionalOnMissingBean
    public GrayEventRetriever grayEventRetriever(
            GrayEventLogModule grayEventLogModule,
            List<EventConverter> eventConverters,
            GrayEventCodec grayEventCodec) {
        return new GrayEventLogRetriever(grayEventLogModule, eventConverters, grayEventCodec);
    }


    @Bean
    public GrayEventInitializer grayEventInitializer() {
        return new GrayEventInitializer();
    }

    @Bean
    public GraServiceEventConverter graServiceEventConverter() {
        return new GraServiceEventConverter();
    }

    @Bean
    public GraTrackEventConverter graTrackEventConverter() {
        return new GraTrackEventConverter();
    }

    @Bean
    public GrayInstanceEventConverter grayInstanceEventConverter(GrayModule grayModule) {
        return new GrayInstanceEventConverter(grayModule);
    }

    @Bean
    public GrayPolicyEventConverter grayPolicyEventConverter(GrayModule grayModule) {
        return new GrayPolicyEventConverter(grayModule);
    }


    @Bean
    public GrayDecisionEventConverter grayDecisionEventConverter(GrayModule grayModule) {
        return new GrayDecisionEventConverter(grayModule);
    }

}
