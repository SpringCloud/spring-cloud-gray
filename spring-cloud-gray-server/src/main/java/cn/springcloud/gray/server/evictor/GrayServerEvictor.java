package cn.springcloud.gray.server.evictor;


import cn.springcloud.gray.server.module.GrayModule;

/**
 *
 */
public interface GrayServerEvictor {

    void evict(GrayModule grayModule);


}
