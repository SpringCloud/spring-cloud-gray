package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpTrackInfo;

import java.util.Collection;
import java.util.Map;

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

            Map<String, ? extends Collection<String>> headers = grayTrackInfo.getHeaders();
            PredicateComparator<Collection<String>> predicateComparator =
                    Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                return false;
            }
            return predicateComparator.test(headers.get(configBean.getHeader()), configBean.getValues());
        };
    }
}
