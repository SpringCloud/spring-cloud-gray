package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.clustering.synchro.ServerSynchronizer;
import cn.springcloud.gray.server.configuration.properties.GrayServerEventProperties;
import cn.springcloud.gray.server.event.triggering.*;
import cn.springcloud.gray.server.event.triggering.converter.*;
import cn.springcloud.gray.server.module.gray.GrayEventLogModule;
import cn.springcloud.gray.server.module.gray.GrayModule;
import cn.springcloud.gray.server.module.gray.GrayServerModule;
import cn.springlcoud.gray.event.codec.GrayEventCodec;
import cn.springlcoud.gray.event.codec.JsonGrayEventCodec;
import cn.springlcoud.gray.event.server.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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


    @Autowired
    private GrayServerEventProperties grayServerEventProperties;

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
    public EventTypeRegistry eventTypeRegistry() throws ClassNotFoundException {
        return new DefaultEventTypeRegistry(grayServerEventProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayEventRetriever grayEventRetriever(
            EventTypeRegistry eventTypeRegistry,
            GrayEventLogModule grayEventLogModule,
            List<EventConverter> eventConverters,
            GrayEventCodec grayEventCodec) {
        return new GrayEventLogRetriever(eventTypeRegistry, grayEventLogModule, eventConverters, grayEventCodec);
    }


    @Bean
    public GrayEventInitializer grayEventInitializer() {
        return new GrayEventInitializer();
    }

    @Bean
    @ConditionalOnMissingBean(name = "graServiceEventConverter")
    public GraServiceEventConverter graServiceEventConverter() {
        return new GraServiceEventConverter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "graTrackEventConverter")
    public GraTrackEventConverter graTrackEventConverter() {
        return new GraTrackEventConverter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "grayInstanceEventConverter")
    public GrayInstanceEventConverter grayInstanceEventConverter(GrayModule grayModule) {
        return new GrayInstanceEventConverter(grayModule);
    }


    @Bean
    @ConditionalOnMissingBean(name = "grayInstanceAliasEventConverter")
    public GrayInstanceAliasEventConverter grayInstanceAliasEventConverter() {
        return new GrayInstanceAliasEventConverter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "grayInstanceRoutePolicyEventConverter")
    public GrayRoutePolicyEventConverter grayInstanceRoutePolicyEventConverter(GrayServerModule grayServerModule) {
        return new GrayRoutePolicyEventConverter(grayServerModule);
    }

    @Bean
    @ConditionalOnMissingBean(name = "grayPolicyEventConverter")
    public GrayPolicyEventConverter grayPolicyEventConverter(GrayModule grayModule) {
        return new GrayPolicyEventConverter(grayModule);
    }


    @Bean
    @ConditionalOnMissingBean(name = "grayDecisionEventConverter")
    public GrayDecisionEventConverter grayDecisionEventConverter(GrayModule grayModule) {
        return new GrayDecisionEventConverter(grayModule);
    }

    @Bean
    @ConditionalOnMissingBean(name = "handleActionEventConverter")
    public HandleActionEventConverter handleActionEventConverter(GrayModule grayModule) {
        return new HandleActionEventConverter(grayModule);
    }

    @Bean
    @ConditionalOnMissingBean(name = "handleEventConverter")
    public HandleEventConverter handleEventConverter() {
        return new HandleEventConverter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "handleRuleEventConverter")
    public HandleRuleEventConverter handleRuleEventConverter(GrayModule grayModule) {
        return new HandleRuleEventConverter(grayModule);
    }

    @Bean
    @ConditionalOnBean(ServerSynchronizer.class)
    public GrayEventServerSynchroObserver grayEventServerSynchroObserver(ServerSynchronizer serverSynchronizer) {
        return new GrayEventServerSynchroObserver(serverSynchronizer);
    }

}
