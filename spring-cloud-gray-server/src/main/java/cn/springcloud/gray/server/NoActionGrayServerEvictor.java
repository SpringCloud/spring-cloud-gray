package cn.springcloud.gray.server;

import cn.springcloud.gray.core.GrayServiceManager;

public class NoActionGrayServerEvictor implements GrayServerEvictor {


    public static NoActionGrayServerEvictor INSTANCE = new NoActionGrayServerEvictor();


    private NoActionGrayServerEvictor() {

    }

    @Override
    public void evict(GrayServiceManager serviceManager) {

    }
}
