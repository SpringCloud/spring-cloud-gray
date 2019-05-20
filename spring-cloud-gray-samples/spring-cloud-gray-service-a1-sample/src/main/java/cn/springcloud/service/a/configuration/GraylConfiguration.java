package cn.springcloud.service.a.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraylConfiguration {

//    @Autowired
//    private GrayDecisionFactoryKeeper grayDecisionFactoryKeeper;
//    @Autowired(required = false)
//    private List<RequestInterceptor> requestInterceptors;
//
//    @Bean
//    public GrayManager grayManager() {
//        return new SimpleGrayManager(grayDecisionFactoryKeeper, requestInterceptors);
//    }


    @Bean(destroyMethod = "shutdown")
    public InitializingBean initializingBean() {
        return new InitializingBean() {

            @Override
            public void afterPropertiesSet() throws Exception {

            }

            public void shutdown() {
                System.out.println("ssssshutdown...");
            }
        };
    }


}
