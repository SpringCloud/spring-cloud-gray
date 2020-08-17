package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.module.user.AuthorityModule;
import cn.springcloud.gray.server.resources.interceptor.AuthorityInterceptor;
import cn.springcloud.gray.server.resources.interceptor.NamespaceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by saleson on 2017/7/5.
 */
@Configuration
@ComponentScan({"cn.springcloud.gray.server.resources"})
@Import(Swagger2Configuration.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    private AuthorityModule authorityModule;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600l);
        corsConfiguration.addExposedHeader("X-Total-Count");
        corsConfiguration.addExposedHeader("X-Pagination");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        new CorsFilter(urlBasedCorsConfigurationSource);

        FilterRegistrationBean filterRegistrationBean =
                new FilterRegistrationBean(new CorsFilter(urlBasedCorsConfigurationSource));
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new NamespaceInterceptor(authorityModule))
                .addPathPatterns("/**");
        registry.addInterceptor(new AuthorityInterceptor(authorityModule)).addPathPatterns("/**");
    }

}
