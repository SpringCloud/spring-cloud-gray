package cn.springcloud.gray;

import cn.springcloud.gray.decision.PolicyDecisionManager;
import cn.springcloud.gray.request.track.GrayTrackHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultGrayManager extends SimpleGrayManager {


    public DefaultGrayManager(
            GrayTrackHolder grayTrackHolder,
            PolicyDecisionManager policyDecisionManager,
            AliasRegistry aliasRegistry) {
        super(grayTrackHolder, policyDecisionManager);
        setAliasRegistry(aliasRegistry);
    }

    @Override
    public boolean hasInstanceGray(String serviceId) {
        return GrayClientHolder.getGraySwitcher().state() && super.hasInstanceGray(serviceId);
    }

    @Override
    public boolean hasServiceGray(String serviceId) {
        return GrayClientHolder.getGraySwitcher().state() && super.hasServiceGray(serviceId);
    }
}
