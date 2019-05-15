package cn.springcloud.gray.client.config;

import cn.springcloud.gray.decision.DefaultGrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.factory.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
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
            List<ConversionService> conversionServices, Validator validator, List<GrayDecisionFactory> decisionFactories) {
        if (CollectionUtils.isNotEmpty(conversionServices)) {
            return new DefaultGrayDecisionFactoryKeeper(conversionServices.get(0), validator, decisionFactories);
        }
        return new DefaultGrayDecisionFactoryKeeper(DefaultConversionService.getSharedInstance(), validator, decisionFactories);

    }


}
