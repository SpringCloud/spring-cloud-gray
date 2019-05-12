package cn.springcloud.gray.client.config;

import cn.springcloud.gray.decision.DefaultGrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.factory.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.Validator;

import java.util.List;

@Configuration
public class GrayDecisionFactoryConfiguration {


    @Configuration
    public static class WebGrayDecisionFactoryConfiguration {

        @Bean
        public HttpHeaderGrayDecisionFactory httpHeaderGrayDecisionFactory() {
            return new HttpHeaderGrayDecisionFactory();
        }

        @Bean
        public HttpMethodGrayDecisionFactory httpMethodGrayDecisionFactory() {
            return new HttpMethodGrayDecisionFactory();
        }

        @Bean
        public HttpParameterGrayDecisionFactory httpParameterGrayDecisionFactory() {
            return new HttpParameterGrayDecisionFactory();
        }

        @Bean
        public TraceIpGrayDecisionFactory traceIpGrayDecisionFactory() {
            return new TraceIpGrayDecisionFactory();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public GrayDecisionFactoryKeeper grayDecisionFactoryKeeper(
            ConversionService conversionService, Validator validator, List<GrayDecisionFactory> decisionFactories) {
        return new DefaultGrayDecisionFactoryKeeper(conversionService, validator, decisionFactories);
    }


}
