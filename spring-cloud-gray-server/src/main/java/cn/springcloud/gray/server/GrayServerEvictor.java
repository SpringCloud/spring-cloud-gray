package cn.springcloud.gray.server;

import cn.springcloud.gray.core.GrayServiceManager;


/**
 *
 */
public interface GrayServerEvictor {

    void evict(GrayServiceManager serviceManager);


}
