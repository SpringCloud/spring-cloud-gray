package cn.springcloud.gray.decision;


import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.bamboo.BambooRequestContext;
import cn.springcloud.gray.core.GrayDecision;

import java.util.Map;
import java.util.Objects;

public class ContextParameterDecision implements GrayDecision {


    private final Map<String, String> params;

    public ContextParameterDecision(Map<String, String> params) {
        if (params.isEmpty()) {
            throw new NullPointerException("params must not be empty");
        }
        this.params = params;
    }

    @Override
    public boolean test(BambooRequest bambooRequest) {
        BambooRequestContext bambooRequestContext = BambooRequestContext.currentRequestCentxt();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!Objects.equals(entry.getValue(), bambooRequestContext.getParameter(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }
}
