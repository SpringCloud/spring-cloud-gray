package cn.springcloud.gray.server.evictor;

import cn.springcloud.gray.server.module.GrayModule;

public class NoActionGrayServerEvictor implements GrayServerEvictor {


    public static NoActionGrayServerEvictor INSTANCE = new NoActionGrayServerEvictor();


    private NoActionGrayServerEvictor() {

    }

    @Override
    public void evict(GrayModule grayModule) {

    }
}
