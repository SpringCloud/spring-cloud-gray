package cn.springcloud.gray.event.listener;

import cn.springcloud.gray.DataSet;
import cn.springcloud.gray.GrayManager;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.local.InstanceLocalInfoObtainer;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;
import cn.springcloud.gray.model.RoutePolicy;
import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.event.RoutePolicyEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author saleson
 * @date 2020-02-03 18:17
 */
@Slf4j
public class RoutePolicyEventListener extends AbstractGrayEventListener<RoutePolicyEvent> {

    private static final String DELETE_CONSUMER_PREFIX = "del_";
    private static final String UPDATE_CONSUMER_PREFIX = "update_";

    private GrayManager grayManager;
    private InstanceLocalInfoObtainer instanceLocalInfoObtainer;
    private Map<String, Consumer<RoutePolicyEvent>> consumerFuncs = new ConcurrentHashMap<>();

    public RoutePolicyEventListener(
            GrayManager grayManager, InstanceLocalInfoObtainer instanceLocalInfoObtainer) {
        this.grayManager = grayManager;
        this.instanceLocalInfoObtainer = instanceLocalInfoObtainer;
        init();
    }


    protected void init() {
        addUpdateConsumerFunc(RoutePolicyType.SERVICE_ROUTE.name(), this::invokeModifyServiceRouteEvent);
        addDeleteConsumerFunc(RoutePolicyType.SERVICE_ROUTE.name(), this::invokeDeleteServiceRouteEvent);
        addUpdateConsumerFunc(RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name(), this::invokeModifyServiceMultiVersionRouteEvent);
        addDeleteConsumerFunc(RoutePolicyType.SERVICE_MULTI_VER_ROUTE.name(), this::invokeDeleteServiceMultiVersionRouteEvent);
        addUpdateConsumerFunc(RoutePolicyType.INSTANCE_ROUTE.name(), this::invokeModifyInstanceRouteEvent);
        addDeleteConsumerFunc(RoutePolicyType.INSTANCE_ROUTE.name(), this::invokeDeleteInstanceRouteEvent);
    }


    public void addUpdateConsumerFunc(String type, Consumer<RoutePolicyEvent> consumer) {
        addConsumerFunc(UPDATE_CONSUMER_PREFIX + type, consumer);
    }

    public void addDeleteConsumerFunc(String type, Consumer<RoutePolicyEvent> consumer) {
        addConsumerFunc(DELETE_CONSUMER_PREFIX + type, consumer);
    }

    public Consumer<RoutePolicyEvent> getUpdateConsumerFunc(String type) {
        return getConsumerFunc(UPDATE_CONSUMER_PREFIX + type);
    }

    public Consumer<RoutePolicyEvent> getDeleteConsumerFunc(String type) {
        return getConsumerFunc(DELETE_CONSUMER_PREFIX + type);
    }

    @Override
    protected void onUpdate(RoutePolicyEvent event) {
        invokeConsumerFunc(getUpdateConsumerFunc(event.getRoutePolicyType()), event);
    }

    @Override
    protected void onDelete(RoutePolicyEvent event) {
        invokeConsumerFunc(getDeleteConsumerFunc(event.getRoutePolicyType()), event);
    }


    @Override
    protected boolean validate(RoutePolicyEvent event) {
        return true;
    }


    protected void invokeConsumerFunc(Consumer<RoutePolicyEvent> consumer, RoutePolicyEvent event) {
        if (Objects.isNull(consumer)) {
            log.warn("type 为'{}'RoutePolicyEvent缺少消费方法, 请检查该类型的Update和Delete消费方法", event.getType());
            return;
        }
        consumer.accept(event);
    }


    protected void addConsumerFunc(String key, Consumer<RoutePolicyEvent> consumer) {
        consumerFuncs.put(key.toLowerCase(), consumer);
    }

    protected Consumer<RoutePolicyEvent> getConsumerFunc(String key) {
        return consumerFuncs.get(key.toLowerCase());
    }


    private void invokeModifyServiceRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayService grayService = grayManager.getGrayService(routePolicy.getModuleId());
            if (Objects.isNull(grayService)) {
                log.warn("没有找到GrayService, serviceId:{}, 创建GrayService", routePolicy.getModuleId());
                grayService = grayManager.createGrayService(routePolicy.getModuleId());
            }
            grayService.getRoutePolicies().addData(String.valueOf(routePolicy.getPolicyId()));
        });
    }

    private void invokeDeleteServiceRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayService grayService = grayManager.getGrayService(routePolicy.getModuleId());
            if (Objects.isNull(grayService)) {
                log.warn("没有找到GrayService, serviceId:{}", routePolicy.getModuleId());
                return;
            }
            grayService.getRoutePolicies().removeData(String.valueOf(routePolicy.getPolicyId()));
        });
    }

    private void invokeModifyServiceMultiVersionRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayService grayService = grayManager.getGrayService(routePolicy.getModuleId());
            if (Objects.isNull(grayService)) {
                log.warn("没有找到GrayService, serviceId:{}", routePolicy.getModuleId());
                return;
            }
            DataSet<String> routePolicies = grayService.getVersionRotePolicies(routePolicy.getResource());
            if (Objects.isNull(routePolicies)) {
                routePolicies = grayService.createVersionRoutePolicies(routePolicy.getResource());
            }
            routePolicies.addData(String.valueOf(routePolicy.getPolicyId()));
        });
    }

    private void invokeDeleteServiceMultiVersionRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayService grayService = grayManager.getGrayService(routePolicy.getModuleId());
            if (Objects.isNull(grayService)) {
                log.warn("没有找到GrayService, serviceId:{}", routePolicy.getModuleId());
                return;
            }
            DataSet<String> routePolicies = grayService.getMultiVersionRotePolicies().get(routePolicy.getResource());
            if (Objects.isNull(routePolicies)) {
                log.warn("没有找到GrayService版本 '{} -- {}'路由策略列表", routePolicy.getModuleId(), routePolicy.getResource());
                return;
            }
            routePolicies.removeData(String.valueOf(routePolicy.getPolicyId()));
        });

    }

    private void invokeModifyInstanceRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayInstance grayInstance = grayManager.getGrayInstance(routePolicy.getModuleId(), routePolicy.getResource());
            if (Objects.isNull(grayInstance)) {
                log.warn("没有找到GrayInstance, serviceId:{}, instanceId:{}", routePolicy.getModuleId(), routePolicy.getResource());
                return;
            }
            grayInstance.addRoutePolicy(String.valueOf(routePolicy.getPolicyId()));
        });
    }

    private void invokeDeleteInstanceRouteEvent(RoutePolicyEvent event) {
        invokeAndFilterSelfService(event.getRoutePolicy(), routePolicy -> {
            GrayInstance grayInstance = grayManager.getGrayInstance(routePolicy.getModuleId(), routePolicy.getResource());
            if (Objects.isNull(grayInstance)) {
                log.warn("没有找到GrayInstance, serviceId:{}, instanceId:{}", routePolicy.getModuleId(), routePolicy.getResource());
                return;
            }
            grayInstance.removeRoutePolicy(String.valueOf(routePolicy.getPolicyId()));
        });
    }

    private void invokeAndFilterSelfService(RoutePolicy routePolicy, Consumer<RoutePolicy> consumer) {
        if (isLocalSelfService(routePolicy.getModuleId())) {
            log.info("接收到本服务的事务, routePolicyId: {}", routePolicy.getId());
            return;
        }
        if (Objects.isNull(consumer)) {
            return;
        }
        consumer.accept(routePolicy);
    }


    private boolean isLocalSelfService(String serviceId) {
        InstanceLocalInfo instanceLocalInfo = instanceLocalInfoObtainer.getInstanceLocalInfo();
        return instanceLocalInfo == null || StringUtils.equals(serviceId, instanceLocalInfo.getServiceId());
    }

}
