package cn.springcloud.gray.server;

import cn.springcloud.gray.server.manager.GrayServiceManager;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PreDestroy;

public class GrayServerInitializingDestroyBean implements InitializingBean {
    private GrayServiceManager grayServiceManager;

    public GrayServerInitializingDestroyBean(GrayServiceManager grayServiceManager) {
        this.grayServiceManager = grayServiceManager;
    }

    @Override
    public void afterPropertiesSet() {
        initToWork();
    }


    private void initToWork() {
        grayServiceManager.openForWork();
    }


    @PreDestroy
    public void shutdown() {
        grayServiceManager.shutdown();
    }
}
