package cn.springcloud.gray.server;


import cn.springcloud.gray.server.configuration.GrayServerMarkerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GrayServerMarkerConfiguration.class)
public @interface EnableGrayServer {
}
