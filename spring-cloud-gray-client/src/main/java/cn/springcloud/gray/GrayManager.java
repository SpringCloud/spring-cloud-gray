package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.model.GrayInstance;
import cn.springcloud.gray.model.GrayService;

import java.util.Collection;
import java.util.List;


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
    boolean hasGray(String serviceId);

    Collection<GrayService> allGrayServices();

    GrayService getGrayService(String serviceId);

    GrayInstance getGrayInstance(String serviceId, String instanceId);

    List<GrayDecision> getGrayDecision(GrayInstance instance);

    List<GrayDecision> getGrayDecision(String serviceId, String instanceId);

    void updateGrayInstance(GrayInstance instance);

    void closeGray(GrayInstance instance);

    void closeGray(String serviceId, String instanceId);

    List<RequestInterceptor> getRequeestInterceptors(String interceptroType);


    void shutdown();
}
