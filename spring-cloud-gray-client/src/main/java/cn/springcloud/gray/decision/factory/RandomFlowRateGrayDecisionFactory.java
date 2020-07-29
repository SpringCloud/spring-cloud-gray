package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 按(value(type,feild)+salt)%100， &lt; rate的将放量。
 * 从以上的逻辑中实现按百分比灰度放量.
 */
@Slf4j
public class RandomFlowRateGrayDecisionFactory extends FlowRateGrayDecisionFactory {

    public RandomFlowRateGrayDecisionFactory() {
        super();
    }

    @Override
    public GrayDecision apply(FlowRateGrayDecisionFactory.Config configBean) {
        return args -> {
            Config config = (Config) configBean;
            GrayHttpRequest grayHttpRequest = (GrayHttpRequest) args.getGrayRequest();
            if (StringUtils.isEmpty(config.getValue())) {
                log.warn("[RandomFlowRateGrayDecision] serviceId:{}, uri:{} 没有获取到必要的config.value, testResult:{}",
                        grayHttpRequest.getServiceId(), grayHttpRequest.getUri(), false);
                return false;
            }
            String value = getFieldValue(grayHttpRequest, configBean);
            if (!validRequestValue(config, value)) {
                if (log.isDebugEnabled()) {
                    log.debug("[RandomFlowRateGrayDecision] serviceId:{}, uri:{} 值未对比成功 - decisionConfig:{}, value:{}, testResult:{}",
                            grayHttpRequest.getServiceId(), grayHttpRequest.getUri(), JsonUtils.toJsonString(config), value, false);
                }
                return false;
            }
            int val = ThreadLocalRandom.current().nextInt(100);
            boolean b = val <= configBean.getRate();
            if (log.isDebugEnabled()) {
                log.debug("[RandomFlowRateGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, value:{}, randomVal:{}, testResult:{}",
                        grayHttpRequest.getServiceId(), grayHttpRequest.getUri(), JsonUtils.toJsonString(config), value, val, b);
            }
            return b;
        };
    }

    private boolean validRequestValue(Config config, String reqValue) {
        return config.isIgnoreCase() ? StringUtils.equalsIgnoreCase(config.getValue(), reqValue) :
                StringUtils.equals(config.getValue(), reqValue);
    }


    @Setter
    @Getter
    public static class Config extends FlowRateGrayDecisionFactory.Config {
        private String value;
        private boolean ignoreCase;
    }

}
