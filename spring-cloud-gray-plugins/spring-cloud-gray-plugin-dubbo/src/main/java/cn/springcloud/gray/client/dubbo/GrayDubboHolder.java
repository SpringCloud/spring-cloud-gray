package cn.springcloud.gray.client.dubbo;

import cn.springcloud.gray.client.dubbo.configuration.properties.GrayDubboProperties;

/**
 * @author saleson
 * @date 2020-09-12 23:42
 */
public class GrayDubboHolder {

    private static GrayDubboProperties grayDubboProperties;

    public static GrayDubboProperties getGrayDubboProperties() {
        return grayDubboProperties;
    }

    public static void setGrayDubboProperties(GrayDubboProperties grayDubboProperties) {
        GrayDubboHolder.grayDubboProperties = grayDubboProperties;
    }
}
