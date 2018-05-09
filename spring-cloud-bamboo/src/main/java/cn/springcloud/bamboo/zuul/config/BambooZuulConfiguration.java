package cn.springcloud.bamboo.zuul.config;

import cn.springcloud.bamboo.autoconfig.properties.BambooProperties;
import cn.springcloud.bamboo.zuul.filter.BambooPostZuulFilter;
import cn.springcloud.bamboo.zuul.filter.BambooPreZuulFilter;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = ZuulServlet.class)
public class BambooZuulConfiguration {
    @Autowired
    private BambooProperties bambooProperties;

    @Bean
    public BambooPreZuulFilter bambooPreZuulFilter(){
        return new BambooPreZuulFilter(bambooProperties);
    }

    @Bean
    public BambooPostZuulFilter bambooPostZuulFilter(){
        return new BambooPostZuulFilter();
    }
}
