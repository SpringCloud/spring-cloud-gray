package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

@Slf4j
public class HttpTrackHeaderGrayDecisionFactory extends CompareGrayDecisionFactory<HttpHeaderGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(HttpTrackHeaderGrayDecisionFactory.class);

    public HttpTrackHeaderGrayDecisionFactory() {
        super(HttpHeaderGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(HttpHeaderGrayDecisionFactory.Config configBean) {
        return args -> {
            GrayHttpRequest grayRequest = (GrayHttpRequest) args.getGrayRequest();
            GrayTrackInfo grayTrackInfo = grayRequest.getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("[HttpTrackHeaderGrayDecision] serviceId:{}, uri:{} 没有获取到灰度追踪信息, testReslut:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), false);
                return false;
            }

            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("[HttpTrackHeaderGrayDecision] serviceId:{}, uri:{} 没有找到相应与compareMode'{}'对应的PredicateComparator, testReslut:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), configBean.getCompareMode(), false);
                return false;
            }

            List<String> headerValues = grayTrackInfo.getHeader(configBean.getHeader());
            boolean b = predicateComparator.test(headerValues, configBean.getValues());
            if (log.isDebugEnabled()) {
                log.debug("[HttpTrackHeaderGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, trackHeader:{}, testReslut:{}",
                        grayRequest.getServiceId(), grayRequest.getUri(), JsonUtils.toJsonString(configBean), headerValues, b);
            }
            return b;
        };
    }
}
