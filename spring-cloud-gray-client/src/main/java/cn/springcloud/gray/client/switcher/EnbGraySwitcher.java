package cn.springcloud.gray.client.switcher;

import cn.springcloud.gray.client.config.properties.GrayProperties;

public class EnbGraySwitcher implements GraySwitcher {

    private GrayProperties grayProperties;

    public EnbGraySwitcher(GrayProperties grayProperties) {
        this.grayProperties = grayProperties;
    }

    @Override
    public boolean judge() {
        return grayProperties.isEnabled();
    }
}
