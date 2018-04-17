package cn.springcloud.gray.server;

import cn.springcloud.gray.core.GrayServiceManager;

public class GrayServerContext {

    public static final String DEFAULT_PREFIX = "gray";

    private static GrayServiceManager grayServiceManager;
    private static GrayServerEvictor grayServerEvictor;


    public static GrayServiceManager getGrayServiceManager() {
        return grayServiceManager;
    }

    static void setGrayServiceManager(GrayServiceManager grayServiceManager) {
        GrayServerContext.grayServiceManager = grayServiceManager;
    }

    public static GrayServerEvictor getGrayServerEvictor() {
        return grayServerEvictor;
    }

    static void setGrayServerEvictor(GrayServerEvictor grayServerEvictor) {
        GrayServerContext.grayServerEvictor = grayServerEvictor;
    }
}
