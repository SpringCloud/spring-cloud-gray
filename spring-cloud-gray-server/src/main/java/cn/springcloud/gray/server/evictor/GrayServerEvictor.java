package cn.springcloud.gray.server.evictor;


import cn.springcloud.gray.server.module.GrayServerModule;

/**
 *
 */
public interface GrayServerEvictor {

    void evict(GrayServerModule grayServerModule);


}
