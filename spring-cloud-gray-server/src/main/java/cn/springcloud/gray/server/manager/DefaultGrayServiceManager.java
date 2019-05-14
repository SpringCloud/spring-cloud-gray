package cn.springcloud.gray.server.manager;

import cn.springcloud.gray.server.configuration.properties.GrayServerProperties;
import cn.springcloud.gray.server.evictor.GrayServerEvictor;
import cn.springcloud.gray.server.module.GrayServerModule;

import java.util.*;


/**
 * 维护一个Map, 用来管理GrayService，key是service id。
 * 并且每隔一段时间就调用EurekaGrayServerEvictor，检查列表中的实例是否下线，将下线的服务从灰度列表中删除。
 */
public class DefaultGrayServiceManager implements GrayServiceManager {


    private GrayServerProperties grayServerProperties;
    private Timer evictionTimer = new Timer("Gray-EvictionTimer", true);
    private GrayServerModule grayServerModule;
    private GrayServerEvictor grayServerEvictor;


    public DefaultGrayServiceManager(GrayServerProperties grayServerProperties, GrayServerModule grayServerModule, GrayServerEvictor grayServerEvictor) {
        this.grayServerProperties = grayServerProperties;
        this.grayServerModule = grayServerModule;
        this.grayServerEvictor = grayServerEvictor;
    }

    @Override
    public GrayServerModule getGrayServerModule() {
        return grayServerModule;
    }

    @Override
    public void openForWork() {
        evictionTimer.schedule(new EvictionTask(),
                grayServerProperties.getEvictionIntervalTimerInMs(),
                grayServerProperties.getEvictionIntervalTimerInMs());
    }

    @Override
    public void shutdown() {
        evictionTimer.cancel();
    }


    protected void evict() {
        grayServerEvictor.evict(getGrayServerModule());
    }


    class EvictionTask extends TimerTask {

        @Override
        public void run() {
            evict();
        }
    }

}
