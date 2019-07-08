package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpTrackInfo;

import java.util.Collection;

public class HttpTrackHeaderGrayDecisionFactory extends CompareGrayDecisionFactory<HttpHeaderGrayDecisionFactory.Config> {

    public HttpTrackHeaderGrayDecisionFactory() {
        super(HttpHeaderGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(HttpHeaderGrayDecisionFactory.Config configBean) {
        return args -> {
            GrayHttpTrackInfo grayTrackInfo = (GrayHttpTrackInfo) args.getGrayRequest().getGrayTrackInfo();
            if (grayTrackInfo == null) {
                return false;
            }

            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                return false;
            }
            return predicateComparator.test(grayTrackInfo.getHeader(configBean.getHeader()), configBean.getValues());
        };
    }
}
