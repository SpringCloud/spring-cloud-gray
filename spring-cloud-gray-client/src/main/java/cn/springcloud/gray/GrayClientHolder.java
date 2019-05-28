package cn.springcloud.gray;

import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;

public class GrayClientHolder {

    private static GrayManager grayManager;
    private static RequestLocalStorage requestLocalStorage;
    private static ServerExplainer<?> serverExplainer;

    public static GrayManager getGrayManager() {
        return grayManager;
    }

    public static void setGrayManager(GrayManager grayManager) {
        GrayClientHolder.grayManager = grayManager;
    }

    public static RequestLocalStorage getRequestLocalStorage() {
        return requestLocalStorage;
    }

    public static void setRequestLocalStorage(RequestLocalStorage requestLocalStorage) {
        GrayClientHolder.requestLocalStorage = requestLocalStorage;
    }

    public static <SERVER> ServerExplainer<SERVER> getServerExplainer() {
        return (ServerExplainer<SERVER>) serverExplainer;
    }

    public static void setServerExplainer(ServerExplainer<?> serverExplainer) {
        GrayClientHolder.serverExplainer = serverExplainer;
    }
}
