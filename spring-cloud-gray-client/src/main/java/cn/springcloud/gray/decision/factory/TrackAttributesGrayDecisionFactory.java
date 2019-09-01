package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.compare.Comparators;
import cn.springcloud.gray.decision.compare.PredicateComparator;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TrackAttributesGrayDecisionFactory extends CompareGrayDecisionFactory<TrackAttributesGrayDecisionFactory.Config> {

    private static final Logger log = LoggerFactory.getLogger(TrackAttributesGrayDecisionFactory.class);

    public TrackAttributesGrayDecisionFactory() {
        super(Config.class);
    }


    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpTrackInfo grayTrackInfo = (GrayHttpTrackInfo) args.getGrayRequest().getGrayTrackInfo();
            if (grayTrackInfo == null) {
                log.warn("没有获取到灰度追踪信息");
                return false;
            }
            PredicateComparator<Collection<String>> predicateComparator = Comparators.getCollectionStringComparator(configBean.getCompareMode());
            if (predicateComparator == null) {
                log.warn("没有找到相应与compareMode'{}'对应的PredicateComparator", configBean.getCompareMode());
                return false;
            }
            return predicateComparator.test(Arrays.asList(grayTrackInfo.getAttribute(configBean.getName())), configBean.getValues());
        };
    }

    @Setter
    @Getter
    public static class Config extends CompareGrayDecisionFactory.CompareConfig {
        private String name;
        private List<String> values;

        public void setValues(List<String> values) {
            this.values = values;
            Collections.sort(values);
        }
    }

}
