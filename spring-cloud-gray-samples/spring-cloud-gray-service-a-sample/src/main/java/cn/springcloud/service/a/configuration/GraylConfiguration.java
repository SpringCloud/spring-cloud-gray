package cn.springcloud.service.a.configuration;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.SimpleGrayManager;
import cn.springcloud.gray.decision.GrayDecisionFactoryKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.List;

//@Configuration
public class GraylConfiguration {

    @Autowired
    private GrayDecisionFactoryKeeper grayDecisionFactoryKeeper;
    @Autowired(required = false)
    private List<RequestInterceptor> requestInterceptors;

    @Bean
    public SimpleGrayManager grayManager() {
        return new SimpleGrayManager(grayDecisionFactoryKeeper, requestInterceptors);
    }


}
