package cn.springcloud.gray;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayInstanceAlias;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.model.ServiceRouteInfo;
import cn.springcloud.gray.request.track.GrayTrackHolder;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 灰度客户端管理器，维护灰度列表，维护自身灰度状态，创建灰度决策对象。
 * 抽象实现类AbstractGrayManager实现了基础的获取灰度列表， 创建灰度决策对象的能力。
 * BaseGrayManger在其基础上进行了扩展，将灰度列表缓存起来，定时从灰度服务端更新灰度列表。
 */
public interface GrayManager {


    /**
     * 判断指定的服务ID是否有灰度实例
     *
     * @param serviceId 服务ID
     * @return has gray instance if true
     */
    boolean hasInstanceGray(String serviceId);


    /**
     * 判断指定服务ID是否有Service级的灰度
     *
     * @param serviceId
     * @return
     */
    default boolean hasServiceGray(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        if (Objects.isNull(grayService)) {
            return false;
        }

        if (!grayService.getRoutePolicies().isEmpty()) {
            return true;
        }

        for (DataSet<String> value : grayService.getMultiVersionRotePolicies().values()) {
            if (!value.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 返回所有的灰度服务
     *
     * @return
     */
    Collection<GrayService> allGrayServices();

    /**
     * 清空所有的灰度服务信息
     */
    void clearAllGrayServices();

    GrayService getGrayService(String serviceId);

    GrayService createGrayService(String serviceId);

    GrayInstance getGrayInstance(String serviceId, String instanceId);

    default Collection<String> getInstanceRoutePolicies(String serviceId, String instanceId) {
        GrayInstance grayInstance = getGrayInstance(serviceId, instanceId);
        if (Objects.isNull(grayInstance)) {
            return Collections.EMPTY_SET;
        }
        return grayInstance.getRoutePolicies();
    }


    default DataSet<String> getServiceRoutePolicies(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        if (Objects.isNull(grayService)) {
            return null;
        }
        return grayService.getRoutePolicies();
    }

    default Map<String, DataSet<String>> getMultiVersionRoutePolicies(String serviceId) {
        GrayService grayService = getGrayService(serviceId);
        if (Objects.isNull(grayService)) {
            return Collections.EMPTY_MAP;
        }
        return grayService.getMultiVersionRotePolicies();
    }

    default DataSet<String> getServiceVersionRoutePolicies(String serviceId, String version) {
        Map<String, DataSet<String>> multiVersionRotePolicies = getMultiVersionRoutePolicies(serviceId);
        return multiVersionRotePolicies.get(version);
    }

    void updateGrayInstance(GrayInstance instance);

    default void updateServiceRouteInfo(ServiceRouteInfo serviceRouteInfo) {
        GrayService grayService = getGrayService(serviceRouteInfo.getServiceId());
        if (Objects.isNull(grayService)) {
            grayService = createGrayService(serviceRouteInfo.getServiceId());
        }
        grayService.getRoutePolicies().addDatas(serviceRouteInfo.getRoutePolicies());
        for (Map.Entry<String, Set<String>> stringSetEntry : serviceRouteInfo.getMultiVersionRoutePolicies().entrySet()) {
            grayService.getOrCreateVersionRotePolicies(stringSetEntry.getKey()).addDatas(stringSetEntry.getValue());
        }
    }

    void closeGray(GrayInstance instance);

    void closeGray(String serviceId, String instanceId);

    List<RequestInterceptor> getRequeestInterceptors(String interceptroType);

    /**
     * 以serviceId -> grayInstances的形式返回所有灰度服务的实例信息
     *
     * @return
     */
    default Map<String, Collection<GrayInstance>> getMapByAllGrayServices() {
        return allGrayServices().stream()
                .collect(Collectors.toMap(GrayService::getServiceId, GrayService::getGrayInstances));
    }

    void setup();

    void shutdown();


    GrayTrackHolder getGrayTrackHolder();

    PolicyDecisionManager getPolicyDecisionManager();

    void setGrayInstanceAlias(GrayInstanceAlias grayInstanceAlias);
}
