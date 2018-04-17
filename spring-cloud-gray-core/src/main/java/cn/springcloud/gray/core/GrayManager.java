package cn.springcloud.gray.core;

import java.util.Collection;
import java.util.List;


/**
 * 灰度客户端管理器，维护灰度列表，维护自身灰度状态，创建灰度决策对象。
 * 抽象实现类AbstractGrayManager实现了基础的获取灰度列表， 创建灰度决策对象的能力。
 * BaseGrayManger在其基础上进行了扩展，将灰度列表缓存起来，定时从灰度服务端更新灰度列表。
 */
public interface GrayManager {

    void openForWork();

    boolean isOpen(String serviceId);

    List<GrayService> listGrayService();

    GrayService grayService(String serviceId);

    GrayInstance grayInstance(String serviceId, String instanceId);

    List<GrayDecision> grayDecision(GrayInstance instance);

    List<GrayDecision> grayDecision(String serviceId, String instanceId);

    void updateGrayServices(Collection<GrayService> grayServices);

    void serviceDownline();
}
