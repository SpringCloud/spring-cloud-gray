package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class HttpTrackParameterGrayDecisionFactory extends CompareGrayDecisionFactory<HttpParameterGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(HttpTrackParameterGrayDecisionFactory.class);

    public HttpTrackParameterGrayDecisionFactory() {
        super(HttpParameterGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(HttpParameterGrayDecisionFactory.Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            GrayTrackInfo grayTrackInfo = grayRequest.getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("[HttpTrackParameterGrayDecision] serviceId:{}, uri:{} 没有获取到灰度追踪信息, testResult:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), false);
                return false;
            }
            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[HttpTrackParameterGrayDecision] serviceId:{}, uri:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testReslut:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), configBean.getCompareMode(), false);
                return false;
            }

            List<String> parameters = grayTrackInfo.getParameter(configBean.getName());
            boolean b = predicateComparator.test(parameters, configBean.getValues());
            if (log.isDebugEnabled()) {
                log.debug("[HttpTrackParameterGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, trackParameter:{}, testReslut:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), JsonUtils.toJsonString(configBean), parameters, b);
            }
            return b;
        };
    }
}
