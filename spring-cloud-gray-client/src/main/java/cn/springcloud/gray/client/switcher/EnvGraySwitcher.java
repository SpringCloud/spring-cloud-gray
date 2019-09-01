package cn.springcloud.gray.client.switcher;

import cn.springcloud.gray.client.config.properties.GrayProperties;

public class EnvGraySwitcher implements GraySwitcher {

    private GrayProperties grayProperties;

    public EnvGraySwitcher(GrayProperties grayProperties) {
        this.grayProperties = grayProperties;
    }

    @Override
    public boolean state() {
        return grayProperties.isEnabled();
    }
}
