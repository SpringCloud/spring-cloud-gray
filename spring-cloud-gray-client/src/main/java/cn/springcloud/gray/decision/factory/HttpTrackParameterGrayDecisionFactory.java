package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpTrackInfo;

import java.util.Collection;

public class HttpTrackParameterGrayDecisionFactory extends CompareGrayDecisionFactory<HttpParameterGrayDecisionFactory.Config> {


    public HttpTrackParameterGrayDecisionFactory() {
        super(HttpParameterGrayDecisionFactory.Config.class);
    }

    @Override
    public GrayDecision apply(HttpParameterGrayDecisionFactory.Config configBean) {
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
            return predicateComparator.test(grayTrackInfo.getParameter(configBean.getName()), configBean.getValues());
        };
    }
}
