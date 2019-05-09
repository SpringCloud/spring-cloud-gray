package cn.springcloud.gray;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.factory.GrayDecisionFactory;
import cn.springcloud.gray.model.GrayInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * GrayManager的抽象实现类实现了基础的获取灰度列表， 创建灰度决策对象的能力
 */
public abstract class AbstractGrayManager implements GrayManager {
    private static final Logger log = LoggerFactory.getLogger(AbstractGrayManager.class);


    private Map<String, GrayDecisionFactory> grayDecisionFactories = new HashMap<>();
    private Map<String, List<RequestInterceptor>> requestInterceptors = new HashMap<>();


    public AbstractGrayManager(List<GrayDecisionFactory> decisionFactories, List<RequestInterceptor> requestInterceptors) {
        initGrayDecisionFactories(decisionFactories);
        initRequestInterceptors(requestInterceptors);
    }


    @Override
    public List<RequestInterceptor> getRequeestInterceptors(String interceptroType) {
        return requestInterceptors.get(interceptroType);
    }


    @Override
    public List<GrayDecision> getGrayDecision(GrayInstance instance) {
        //todo
        return null;
    }

    @Override
    public List<GrayDecision> getGrayDecision(String serviceId, String instanceId) {
        return getGrayDecision(getGrayInstance(serviceId, instanceId));
    }


    private void initGrayDecisionFactories(List<GrayDecisionFactory> decisionFactories) {
        decisionFactories.stream().forEach(factory -> {
            grayDecisionFactories.put(factory.name(), factory);
        });

    }

    private void initRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
        for (RequestInterceptor interceptor : requestInterceptors) {
            List<RequestInterceptor> interceptors = getRequeestInterceptors(interceptor.interceptroType());
            if (interceptors == null) {
                interceptors = new ArrayList<>();
                this.requestInterceptors.put(interceptor.interceptroType(), interceptors);
            }
            interceptors.add(interceptor);
        }
    }

}
