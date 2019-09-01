package cn.springcloud.gray;

import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerListProcessor;

public class GrayClientHolder {

    private static GrayManager grayManager;
    private static RequestLocalStorage requestLocalStorage;
    private static LocalStorageLifeCycle localStorageLifeCycle;
    private static ServerExplainer<?> serverExplainer;
    private static ServerListProcessor<?> serverListProcessor;
    private static GraySwitcher graySwitcher = new GraySwitcher.DefaultGraySwitcher();
    private static InstanceLocalInfo instanceLocalInfo;
    private static ServerChooser<?> serverChooser;

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

    public static void setLocalStorageLifeCycle(LocalStorageLifeCycle localStorageLifeCycle) {
        GrayClientHolder.localStorageLifeCycle = localStorageLifeCycle;
    }

    public static LocalStorageLifeCycle getLocalStorageLifeCycle() {
        return localStorageLifeCycle;
    }

    public static <SERVER> ServerExplainer<SERVER> getServerExplainer() {
        return (ServerExplainer<SERVER>) serverExplainer;
    }

    public static void setServerExplainer(ServerExplainer<?> serverExplainer) {
        GrayClientHolder.serverExplainer = serverExplainer;
    }

    public static void setServerListProcessor(ServerListProcessor<?> serverListProcessor) {
        GrayClientHolder.serverListProcessor = serverListProcessor;
    }

    public static <SERVER> ServerListProcessor<SERVER> getServereListProcessor() {
        return (ServerListProcessor<SERVER>) serverListProcessor;
    }

    public static GraySwitcher getGraySwitcher() {
        return graySwitcher;
    }

    public static void setGraySwitcher(GraySwitcher graySwitcher) {
        GrayClientHolder.graySwitcher = graySwitcher;
    }

    public static InstanceLocalInfo getInstanceLocalInfo() {
        return instanceLocalInfo;
    }

    public static void setInstanceLocalInfo(InstanceLocalInfo instanceLocalInfo) {
        GrayClientHolder.instanceLocalInfo = instanceLocalInfo;
    }


    public static <SERVER> ServerChooser<SERVER> getServerChooser() {
        return (ServerChooser<SERVER>) serverChooser;
    }

    public static void setServerChooser(ServerChooser<?> serverChooser) {
        GrayClientHolder.serverChooser = serverChooser;
    }
}
