package cn.springcloud.gray.client;

import cn.springcloud.gray.InstanceLocalInfo;
import cn.springcloud.gray.core.GrayManager;

public class GrayClientAppContext {
    private static GrayManager grayManager;
    private static InstanceLocalInfo instanceLocalInfo;


    public static GrayManager getGrayManager() {
        return grayManager;
    }

    static void setGrayManager(GrayManager grayManager) {
        GrayClientAppContext.grayManager = grayManager;
    }


    public static InstanceLocalInfo getInstanceLocalInfo() {
        return instanceLocalInfo;
    }

    static void setInstanceLocalInfo(InstanceLocalInfo instanceLocalInfo) {
        GrayClientAppContext.instanceLocalInfo = instanceLocalInfo;
    }
}
