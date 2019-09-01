package cn.springcloud.gray.server.evictor;


import cn.springcloud.gray.server.module.gray.GrayServerModule;

/**
 *
 */
public interface GrayServerEvictor {

    void evict(GrayServerModule grayServerModule);


}
