package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class HttpTrackHeaderGrayDecisionFactory extends CompareGrayDecisionFactory<HttpHeaderGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(HttpTrackHeaderGrayDecisionFactory.class);

    public HttpTrackHeaderGrayDecisionFactory() {
        super(HttpHeaderGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(HttpHeaderGrayDecisionFactory.Config configBean) {
        return args -> {
            GrayHttpTrackInfo grayTrackInfo = (GrayHttpTrackInfo) args.getGrayRequest().getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("没有获取到灰度追踪信息");
                return false;
            }

            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("没有找到相应与compareMode'{}'对应的PredicateComparator", configBean.getCompareMode());
                return false;
            }
            return predicateComparator.test(grayTrackInfo.getHeader(configBean.getHeader()), configBean.getValues());
        };
    }
}
