package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
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
    private Map<String, GrayInstance> grayInstances = new ConcurrentHashMap<>();
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

}
