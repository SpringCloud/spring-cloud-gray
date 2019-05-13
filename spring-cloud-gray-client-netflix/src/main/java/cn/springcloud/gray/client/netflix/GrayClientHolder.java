package cn.springcloud.gray.client.netflix;

import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.request.RequestLocalStorage;
import com.netflix.loadbalancer.Server;

public class GrayClientHolder {

    private static GrayManager grayManager;
    private static RequestLocalStorage requestLocalStorage;
    private static ServerExplainer<Server> serverExplainer;

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

    public static ServerExplainer<Server> getServerExplainer() {
        return serverExplainer;
    }

    public static void setServerExplainer(ServerExplainer<Server> serverExplainer) {
        GrayClientHolder.serverExplainer = serverExplainer;
    }
}
