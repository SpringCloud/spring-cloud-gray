package cn.springcloud.gray.client.config;

import cn.springcloud.gray.decision.DefaultGrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import cn.springcloud.gray.decision.factory.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

        @Bean
        public HttpTrackParameterGrayDecisionFactory httpTrackParameterGrayDecisionFactory() {
            return new HttpTrackParameterGrayDecisionFactory();
        }

        @Bean
        public HttpTrackHeaderGrayDecisionFactory httpTrackHeaderGrayDecisionFactory() {
            return new HttpTrackHeaderGrayDecisionFactory();
        }

        @Bean
        public TrackAttributeGrayDecisionFactory trackAttributeGrayDecisionFactory() {
            return new TrackAttributeGrayDecisionFactory();
        }
    }


    /**
     * 不可引入spring mvc中的ConversionService， 否则会导致feign 加载时，找不到ServletContext， 从而出现异常:No ServletContext set
     *
     * @param validator         校验器
     * @param decisionFactories 灰度决策工厂类列表
     * @return 灰度决策工厂管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GrayDecisionFactoryKeeper grayDecisionFactoryKeeper(
            /*List<ConversionService> conversionServices, */
            Validator validator, List<GrayDecisionFactory> decisionFactories) {
//        if (CollectionUtils.isNotEmpty(conversionServices)) {
//            return new DefaultGrayDecisionFactoryKeeper(conversionServices.get(0), validator, decisionFactories);
//        }
        return new DefaultGrayDecisionFactoryKeeper(DefaultConversionService.getSharedInstance(), validator, decisionFactories);

    }


}
