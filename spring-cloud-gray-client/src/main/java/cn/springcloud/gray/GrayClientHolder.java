package cn.springcloud.gray;

import cn.springcloud.gray.changed.notify.ChangedNotifyDriver;
import cn.springcloud.gray.choose.ServerChooser;
import cn.springcloud.gray.choose.ServerDistinguisher;
import cn.springcloud.gray.choose.loadbalance.factory.LoadBalancerFactory;
import cn.springcloud.gray.client.switcher.GraySwitcher;
import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import cn.springcloud.gray.request.RequestLocalStorage;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import cn.springcloud.gray.routing.connectionpoint.RoutingConnectionPoint;
import cn.springcloud.gray.servernode.ServerExplainer;
import cn.springcloud.gray.servernode.ServerListProcessor;
import cn.springcloud.gray.spring.SpringEventPublisher;

import java.util.Objects;

public class GrayClientHolder {

    private static GrayManager grayManager;
    private static GrayTrackHolder grayTrackHolder;
    private static PolicyDecisionManager policyDecisionManager;
    private static RequestLocalStorage requestLocalStorage;
    private static LocalStorageLifeCycle localStorageLifeCycle;
    private static RoutingConnectionPoint routingConnectionPoint;
    private static ServerExplainer<?> serverExplainer;
    private static ServerListProcessor<?> serverListProcessor;
    private static GraySwitcher graySwitcher = new GraySwitcher.DefaultGraySwitcher();
    private static InstanceLocalInfo instanceLocalInfo;
    private static ServerChooser<?> serverChooser;
    private static ServerDistinguisher<?> serverDistinguisher;
    private static LoadBalancerFactory loadBalancerFactory;

    private static SpringEventPublisher springEventPublisher;

    private static ChangedNotifyDriver changedNotifyDriver;

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

    public static RoutingConnectionPoint getRoutingConnectionPoint() {
        return routingConnectionPoint;
    }

    public static void setRoutingConnectionPoint(RoutingConnectionPoint routingConnectionPoint) {
        GrayClientHolder.routingConnectionPoint = routingConnectionPoint;
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

    public static <SERVER> ServerDistinguisher<SERVER> getServerDistinguisher() {
        return (ServerDistinguisher<SERVER>) serverDistinguisher;
    }

    public static void setServerDistinguisher(ServerDistinguisher<?> serverDistinguisher) {
        GrayClientHolder.serverDistinguisher = serverDistinguisher;
    }

    public static GrayTrackHolder getGrayTrackHolder() {
        return grayTrackHolder;
    }

    public static void setGrayTrackHolder(GrayTrackHolder grayTrackHolder) {
        GrayClientHolder.grayTrackHolder = grayTrackHolder;
    }

    public static PolicyDecisionManager getPolicyDecisionManager() {
        return policyDecisionManager;
    }

    public static void setPolicyDecisionManager(PolicyDecisionManager policyDecisionManager) {
        GrayClientHolder.policyDecisionManager = policyDecisionManager;
    }

    public static SpringEventPublisher getSpringEventPublisher() {
        return springEventPublisher;
    }

    public static void setSpringEventPublisher(SpringEventPublisher springEventPublisher) {
        GrayClientHolder.springEventPublisher = springEventPublisher;
    }

    public static LoadBalancerFactory getLoadBalancerFactory() {
        return loadBalancerFactory;
    }

    public static void setLoadBalancerFactory(LoadBalancerFactory loadBalancerFactory) {
        GrayClientHolder.loadBalancerFactory = loadBalancerFactory;
    }

    public static ChangedNotifyDriver getChangedNotifyDriver() {
        return changedNotifyDriver;
    }

    public static void setChangedNotifyDriver(ChangedNotifyDriver changedNotifyDriver) {
        GrayClientHolder.changedNotifyDriver = changedNotifyDriver;
    }


    /**
     * 获取当前remote request的service id
     *
     * @return
     */
    public static String getCurrentRequestServiceId() {
        GrayRequest grayRequest = getRequestLocalStorage().getGrayRequest();
        return Objects.isNull(grayRequest) ? null : grayRequest.getServiceId();
    }
}
