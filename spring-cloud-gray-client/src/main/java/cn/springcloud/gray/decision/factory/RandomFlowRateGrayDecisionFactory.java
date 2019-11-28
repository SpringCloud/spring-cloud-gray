package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayHttpRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 按(value(type,feild)+salt)%100， &lt; rate的将放量。
 * 从以上的逻辑中实现按百分比灰度放量.
 */
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
                return false;
            }
            String value = getFieldValue(grayHttpRequest, configBean);

            if (!validRequestValue(config, value)) {
                return false;
            }
            int val = ThreadLocalRandom.current().nextInt(100);
            return val <= configBean.getRate();
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
