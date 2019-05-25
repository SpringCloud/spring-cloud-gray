package cn.springcloud.gray.client.netflix.configuration;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.client.netflix.connectionpoint.DefaultHystrixRibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.connectionpoint.RibbonConnectionPoint;
import cn.springcloud.gray.client.netflix.hystrix.HystrixRequestLocalStorage;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.hystrix.HystrixCommand;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnBean(GrayManager.class)
@ConditionalOnClass({HystrixCommand.class, HystrixFeign.class})
@Import(HystrixGrayTrackWebConfiguration.class)
public class HystrixGrayAutoConfiguration {


    @Autowired
    private GrayManager grayManager;
    @Autowired
    private RequestLocalStorage requestLocalStorage;


    @Bean
    public RequestLocalStorage requestLocalStorage() {
        return new HystrixRequestLocalStorage();
    }


    /**
     * 支持hystrix使用线程隔离时依然能够进行跑线程传递GrayRequest
     *
     * @return DefaultHystrixRibbonConnectionPoint
     */
    @Bean
    public RibbonConnectionPoint hystrixRibbonConnectionPoint() {
        return new DefaultHystrixRibbonConnectionPoint(grayManager, requestLocalStorage);
    }

}
