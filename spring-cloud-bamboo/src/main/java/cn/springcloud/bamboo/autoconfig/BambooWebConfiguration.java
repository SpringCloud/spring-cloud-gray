package cn.springcloud.bamboo.autoconfig;

import cn.springcloud.bamboo.web.IpKeepInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class BambooWebConfiguration  extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpKeepInterceptor());
        super.addInterceptors(registry);
    }
}
