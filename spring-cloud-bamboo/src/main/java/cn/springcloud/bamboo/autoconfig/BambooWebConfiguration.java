package cn.springcloud.bamboo.autoconfig;

import cn.springcloud.bamboo.web.IpKeepInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class BambooWebConfiguration  implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpKeepInterceptor());
    }
}
