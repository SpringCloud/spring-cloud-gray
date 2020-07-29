package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackAttributeGrayDecisionFactory extends CompareGrayDecisionFactory<TrackAttributeGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TrackAttributeGrayDecisionFactory.class);

    public TrackAttributeGrayDecisionFactory() {
        super(TrackAttributeGrayDecisionFactory.Config.class);
    }


    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayRequest grayRequest = args.getGrayRequest();
            GrayTrackInfo grayTrackInfo = grayRequest.getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("[TrackAttributeGrayDecision] serviceId:{} 没有获取到灰度追踪信息, testResult:{}",
                        grayRequest.getServiceId(), false);
                return false;
            }
            PredicateComparator<String> predicateComparator = Comparators.getStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[TrackAttributeGrayDecision] serviceId:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testResult:{}",
                        grayRequest.getServiceId(), configBean.getCompareMode(), false);
                return false;
            }

            String attributeValue = grayTrackInfo.getAttribute(configBean.getName());
            boolean b = predicateComparator.test(attributeValue, configBean.getValue());
            if (log.isDebugEnabled()) {
                log.debug("[TrackAttributeGrayDecision] serviceId:{}, decisionConfig:{}, attributeValue:{}, testReslut:{}",
                        grayRequest.getServiceId(), JsonUtils.toJsonString(configBean), attributeValue, b);
            }
            return b;
        };
    }

    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String name;
        private String value;

    }

}
