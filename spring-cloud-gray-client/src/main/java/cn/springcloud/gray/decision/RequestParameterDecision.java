package cn.springcloud.gray.decision;

import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.gray.core.GrayDecision;
import org.apache.commons.collections.ListUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public class RequestParameterDecision implements GrayDecision {


    private final MultiValueMap<String, String> params;

    public RequestParameterDecision(MultiValueMap<String, String> params) {
        if (params.isEmpty()) {
            throw new NullPointerException("params must not be empty");
        }
        this.params = params;
    }

    @Override
    public boolean test(BambooRequest bambooRequest) {
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            List<String> values = bambooRequest.getParams().get(entry.getKey());
            if (!ListUtils.isEqualList(entry.getValue(), values)) {
                return false;
            }
        }
        return true;
    }
}
