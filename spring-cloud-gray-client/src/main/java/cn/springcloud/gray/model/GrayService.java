package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 注册的服务， 维护一个实例列表。
 */
public class GrayService {
    @Setter
    @Getter
    private String serviceId;
    private Map<String, GrayInstance> grayInstances = new ConcurrentHashMap<>();


    public Collection<GrayInstance> getGrayInstances() {
        return grayInstances.values();
    }


    public boolean contains(String instanceId) {
        return grayInstances.containsKey(instanceId);
    }

    public void setGrayInstance(GrayInstance grayInstance) {
        grayInstances.put(grayInstance.getInstanceId(), grayInstance);
    }

    public GrayInstance removeGrayInstance(String instanceId) {
        return grayInstances.remove(instanceId);
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
