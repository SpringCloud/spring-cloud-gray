package cn.springcloud.gray.client.dubbo.servernode;

import cn.springcloud.gray.AliasRegistry;
import cn.springcloud.gray.utils.StringUtils;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saleson
 * @date 2020-09-10 14:24
 */
public class ServiceInstanceExtractor {

    private static final String protocolsPrefix = "dubbo.protocols";

    private static final Pattern protocolsPattern = Pattern.compile("^dubbo\\.protocols\\.(.+)\\.port$");

    private AliasRegistry aliasRegistry;
    private ServiceInstanceIdExtractor serviceInstanceIdExtractor;
    private volatile Map<String, Map<String, ServiceInstance>> services = new HashMap<>();


    public ServiceInstanceExtractor(AliasRegistry aliasRegistry, ServiceInstanceIdExtractor serviceInstanceIdExtractor) {
        this.aliasRegistry = aliasRegistry;
        this.serviceInstanceIdExtractor = serviceInstanceIdExtractor;
    }

    public String getServiceInstanceId(String serviceId, String alias) {
        AliasRegistry.AliasRegion aliasRegion = createServiceRegion(serviceId);
        return aliasRegistry.getId(aliasRegion, alias);
    }

    public ServiceInstance getServiceInstanceByAlias(String serviceId, String alias) {
        String instanceId = getServiceInstanceId(serviceId, alias);
        return Objects.isNull(instanceId) ? null : getServiceInstance(serviceId, instanceId);
    }

    public ServiceInstance getServiceInstance(String serviceId, String instanceId) {
        Map<String, ServiceInstance> serviceInstances = services.get(serviceId);
        return Objects.isNull(serviceInstances) ? null : serviceInstances.get(instanceId);
    }


    public ServiceInstance removeServiceInstance(String serviceId, String instanceId) {
        Map<String, ServiceInstance> serviceInstances = getServiceInstances(serviceId);
        if (Objects.isNull(serviceInstances) || !serviceInstances.containsKey(instanceId)) {
            return null;
        }
        ServiceInstance serviceInstance = sync(() -> {
            Map<String, ServiceInstance> map = getServiceInstances(serviceId);
            if (Objects.isNull(map)) {
                return null;
            }
            return map.remove(instanceId);
        });

        if (Objects.isNull(serviceInstance)) {
            return null;
        }

        removeServiceInstanceAliases(serviceInstance);

        return serviceInstance;
    }

    public Map<String, ServiceInstance> removeService(String serviceId) {
        if (!services.containsKey(serviceId)) {
            return null;
        }
        Map<String, ServiceInstance> serviceInstances = sync(() -> {
            return services.remove(serviceId);
        });
        if (Objects.isNull(serviceInstances)) {
            return null;
        }
        removeServiceInstancesAliases(serviceId);
        return serviceInstances;
    }

    public void removeServiceInstance(ServiceInstance serviceInstance) {
        removeServiceInstance(serviceInstance.getServiceId(), getServiceInstanceId(serviceInstance));
    }

    public void putServiceInstance(ServiceInstance serviceInstance) {
        synchronized (this) {
            Map<String, ServiceInstance> serviceInstances = getServiceInstances(serviceInstance.getServiceId());
            if (Objects.isNull(serviceInstances)) {
                serviceInstances = new HashMap<>();
                services.put(serviceInstance.getServiceId(), serviceInstances);
            }
            serviceInstances.put(getServiceInstanceId(serviceInstance), serviceInstance);
        }

        addServiceInstanceAliases(serviceInstance);
    }

    public void putServiceInstance(String serviceId, Collection<ServiceInstance> serviceInstances) {
        synchronized (this) {
            Map<String, ServiceInstance> serviceInstanceMap = getOrCreateServiceInstances(serviceId);
            serviceInstances.forEach(serviceInstance -> {
                serviceInstanceMap.put(getServiceInstanceId(serviceInstance), serviceInstance);
            });
        }

        serviceInstances.forEach(this::addServiceInstanceAliases);
    }

    public Map<String, ServiceInstance> getServiceInstances(String serviceId) {
        return services.get(serviceId);
    }


    private synchronized Map<String, ServiceInstance> getOrCreateServiceInstances(String serviceId) {
        Map<String, ServiceInstance> serviceInstances = getServiceInstances(serviceId);
        if (Objects.isNull(serviceInstances)) {
            serviceInstances = new HashMap<>();
            services.put(serviceId, serviceInstances);
        }
        return serviceInstances;
    }


    private synchronized <T> T sync(Supplier<T> supplier) {
        return supplier.get();
    }

    private void addServiceInstanceAliases(ServiceInstance serviceInstance) {
        String[] aliases = getAliases(serviceInstance.getHost(), serviceInstance.getMetadata());
        AliasRegistry.AliasRegion aliasRegion = createServiceRegion(serviceInstance.getServiceId());
        String instanceId = getServiceInstanceId(serviceInstance);
        aliasRegistry.setAliases(aliasRegion, instanceId, aliases);
    }

    private String getServiceInstanceId(ServiceInstance serviceInstance) {
        return serviceInstanceIdExtractor.getInstanceId(serviceInstance);
    }

    private void removeServiceInstanceAliases(ServiceInstance serviceInstance) {
        String[] aliases = getAliases(serviceInstance.getHost(), serviceInstance.getMetadata());
        AliasRegistry.AliasRegion aliasRegion = createServiceRegion(serviceInstance.getServiceId());
        aliasRegistry.removeAliases(aliasRegion, aliases);
    }

    private void removeServiceInstancesAliases(String serviceId) {
        aliasRegistry.removeAlias(createServiceRegion(serviceId));
    }


    private String[] getAliases(String host, Map<String, String> metadata) {
        return metadata.entrySet()
                .stream()
                .map(entry -> getAlias(host, entry.getKey(), entry.getValue()))
                .filter(Objects::nonNull)
                .toArray(String[]::new);
    }


    private String getAlias(String host, String name, String value) {
        if (!isDubboProtocol(name)) {
            return null;
        }
        String protocel = getDubboProtocol(name);
        if (StringUtils.isEmpty(protocel)) {
            return null;
        }
        return protocel + ":" + host + ":" + value;
    }

    private boolean isDubboProtocol(String name) {
        return name.startsWith(protocolsPrefix);
    }

    private String getDubboProtocol(String name) {
        Matcher matcher = protocolsPattern.matcher(name);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }


    private AliasRegistry.AliasRegion createServiceRegion(String serviceId) {
        return AliasRegistry.aliasRegion(
                AliasRegistry.ALIAS_REGION_TYPE_SERVICE, serviceId);
    }


}
