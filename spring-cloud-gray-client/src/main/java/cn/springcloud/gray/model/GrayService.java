package cn.springcloud.gray.model;

import cn.springcloud.gray.DataSet;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 注册的服务， 维护一个实例列表。
 */
public class GrayService {
    @Setter
    @Getter
    private String serviceId;
    private final Map<String, GrayInstance> grayInstances = new ConcurrentHashMap<>();
    private final DataSet<String> routePolicies = new DataSet<>();
    private final Map<String, DataSet<String>> multiVersionRotePolicies = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();


    public Collection<GrayInstance> getGrayInstances() {
        return grayInstances.values();
    }


    public boolean contains(String instanceId) {
        return grayInstances.containsKey(instanceId);
    }

    public void setGrayInstance(GrayInstance grayInstance) {
        lock.lock();
        try {
            grayInstances.put(grayInstance.getInstanceId(), grayInstance);
        } finally {
            lock.unlock();
        }
    }

    public GrayInstance removeGrayInstance(String instanceId) {
        lock.lock();
        try {
            return grayInstances.remove(instanceId);
        } finally {
            lock.unlock();
        }
    }


    public GrayInstance getGrayInstance(String instanceId) {
        return grayInstances.get(instanceId);
    }

    public boolean isOpenGray() {
        return getGrayInstances() != null
                && !getGrayInstances().isEmpty()
                && hasGrayInstance();
    }

    public boolean hasGrayInstance() {
        for (GrayInstance grayInstance : getGrayInstances()) {
            if (grayInstance.isGray()) {
                return true;
            }
        }
        return false;
    }

    public DataSet<String> getRoutePolicies() {
        return routePolicies;
    }

    public synchronized DataSet<String> createVersionRoutePolicies(String version) {
        DataSet<String> versionRoutePolicies = getVersionRotePolicies(version);
        if (Objects.nonNull(versionRoutePolicies)) {
            return versionRoutePolicies;
        }
        versionRoutePolicies = new DataSet<>();
        multiVersionRotePolicies.put(version, versionRoutePolicies);
        return versionRoutePolicies;
    }

    public DataSet<String> getVersionRotePolicies(String version) {
        return multiVersionRotePolicies.get(version);
    }

    public Map<String, DataSet<String>> getMultiVersionRotePolicies() {
        return Collections.unmodifiableMap(multiVersionRotePolicies);
    }
}
