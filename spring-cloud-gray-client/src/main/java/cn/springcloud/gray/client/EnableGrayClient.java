package cn.springcloud.gray.client;

import cn.springcloud.gray.client.config.GrayClientImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(GrayClientImportSelector.class)
public @interface EnableGrayClient {


}
