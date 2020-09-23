package cn.springcloud.gray.client.dubbo;

import cn.springcloud.gray.client.dubbo.configuration.properties.GrayDubboProperties;
import cn.springcloud.gray.code.component.initializer.SpringInitializer;

/**
 * @author saleson
 * @date 2020-09-12 23:48
 */
public class GrayDubboInitializer extends SpringInitializer {


    @Override
    protected void initialization() {
        GrayDubboHolder.setGrayDubboProperties(getBean(GrayDubboProperties.class));
    }
}
