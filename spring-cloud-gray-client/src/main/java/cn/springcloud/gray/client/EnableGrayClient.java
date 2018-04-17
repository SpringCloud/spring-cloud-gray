package cn.springcloud.gray.client;

import cn.springcloud.gray.client.config.GrayClientMarkerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(GrayClientMarkerConfiguration.class)
public @interface EnableGrayClient {


}
