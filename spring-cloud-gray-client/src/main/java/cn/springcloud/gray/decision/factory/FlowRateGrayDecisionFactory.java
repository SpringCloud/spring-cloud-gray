package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 按(value(type,feild)+salt).hashcode % 100， &lt; rate的将放量。
 * 从以上的逻辑中实现按百分比灰度放量.
 */
@Slf4j
public class FlowRateGrayDecisionFactory extends AbstractGrayDecisionFactory<FlowRateGrayDecisionFactory.Config> {

    public static final String FIELD_SCOPE_HTTP_HEADER = "HttpHeader";
    public static final String FIELD_SCOPE_HTTP_PARAMETER = "HttpParameter";
    public static final String FIELD_SCOPE_TRACK_ATTRIBUTE = "TrackAttribute";
    public static final String FIELD_SCOPE_HTTP_TRACK_HEADER = "HttpTrackHeader";
    public static final String FIELD_SCOPE_HTTP_TRACK_PARAMETER = "HttpTrackParameter";

    private Map<String, BiFunction<GrayHttpRequest, String, String>> fieldValueGetters = new HashMap<>();

    public FlowRateGrayDecisionFactory() {
        super(FlowRateGrayDecisionFactory.Config.class);
        initFieldGetters();
    }

    @Override
    public GrayDecision apply(Config configBean) {
        return args -> {
            GrayHttpRequest grayHttpRequest = (GrayHttpRequest) args.getGrayRequest();
            String value = getFieldValue(grayHttpRequest, configBean);
            if (StringUtils.isEmpty(value)) {
                log.debug("[FlowRateGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, testResult:{}",
                        grayHttpRequest.getServiceId(), grayHttpRequest.getUri(), configBean, false);
                return false;
            }
            int hashcode = Math.abs((value + StringUtils.defaultString(configBean.getSalt())).hashCode());
            int mod = hashcode % 100;
            boolean b = mod <= configBean.getRate();
            log.debug("[FlowRateGrayDecision] serviceId:{}, uri:{}, decisionConfig:{}, mod:{}, testResult:{}",
                    grayHttpRequest.getServiceId(), grayHttpRequest.getUri(), configBean, mod, b);
            return b;
        };
    }

    private void initFieldGetters() {
        fieldValueGetters.put(FIELD_SCOPE_HTTP_HEADER,
                (grayReq, field) -> getValueForMultiValueMap(grayReq.getHeaders(), field));

        fieldValueGetters.put(FIELD_SCOPE_HTTP_PARAMETER,
                (grayReq, field) -> getValueForMultiValueMap(grayReq.getParameters(), field));

        fieldValueGetters.put(FIELD_SCOPE_TRACK_ATTRIBUTE,
                (grayReq, field) -> grayReq.getGrayTrackInfo().getAttributes().get(field));

        fieldValueGetters.put(FIELD_SCOPE_HTTP_TRACK_HEADER,
                (grayReq, field) -> {
                    GrayTrackInfo grayTrackInfo = grayReq.getGrayTrackInfo();
                    if (!Objects.isNull(grayTrackInfo)) {
                        return joinString(grayTrackInfo.getHeader(field));
                    }
                    return null;
                });

        fieldValueGetters.put(FIELD_SCOPE_HTTP_TRACK_PARAMETER,
                (grayReq, field) -> {
                    GrayTrackInfo grayTrackInfo = grayReq.getGrayTrackInfo();
                    if (!Objects.isNull(grayTrackInfo)) {
                        return joinString(grayTrackInfo.getParameter(field));
                    }
                    return null;
                });
    }

    protected String getFieldValue(GrayHttpRequest grayRequest, Config configBean) {
        BiFunction<GrayHttpRequest, String, String> func = fieldValueGetters.get(configBean.getType());
        if (!Objects.isNull(func)) {
            return func.apply(grayRequest, configBean.getField());
        }
        return null;
    }

    private String getValueForMultiValueMap(Map<String, ? extends Collection<String>> map, String field) {
        return joinString(map.get(field));
    }

    private String joinString(Collection<String> collection) {
        if (collection != null) {
            return StringUtils.join(collection, ",");
        }
        return null;
    }

    @Setter
    @Getter
    @ToString
    public static class Config {
        private String type;
        private String field;
        private String salt;
        private int rate;
    }
}
