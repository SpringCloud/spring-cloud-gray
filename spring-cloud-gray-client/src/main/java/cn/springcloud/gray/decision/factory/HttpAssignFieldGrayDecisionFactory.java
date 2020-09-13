package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.request.GrayHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class HttpAssignFieldGrayDecisionFactory<C extends AbstractAssignFieldGrayDecisionFactory.AssignFieldConfig> extends AbstractAssignFieldGrayDecisionFactory<C> {


    public static final String FIELD_SCOPE_HTTP_HEADER = "HttpHeader";
    public static final String FIELD_SCOPE_HTTP_PARAMETER = "HttpParameter";
    public static final String FIELD_SCOPE_HTTP_TRACK_HEADER = "HttpTrackHeader";
    public static final String FIELD_SCOPE_HTTP_TRACK_PARAMETER = "HttpTrackParameter";

    private static Map<String, BiFunction<GrayHttpRequest, String, ?>> fieldValueGetters = new HashMap<>();

    protected HttpAssignFieldGrayDecisionFactory(Class<C> configClass) {
        super(configClass);
    }


    protected Object getRequestAssignFieldValue(GrayHttpRequest grayRequest, C configBean) {
        Object fieldValue = super.getRequestAssignFieldValue(grayRequest, configBean);
        if (fieldValue != null) {
            return fieldValue;
        }
        switch (configBean.getType()) {
            case FIELD_SCOPE_HTTP_HEADER:
                return grayRequest.getHeader(configBean.getField());
            case FIELD_SCOPE_HTTP_PARAMETER:
                return grayRequest.getParameter(configBean.getField());
            case FIELD_SCOPE_HTTP_TRACK_HEADER:
                if (grayRequest.getGrayTrackInfo() == null) {
                    return null;
                }
                return grayRequest.getGrayTrackInfo().getHeader(configBean.getField());
            case FIELD_SCOPE_HTTP_TRACK_PARAMETER:
                if (grayRequest.getGrayTrackInfo() == null) {
                    return null;
                }
                return grayRequest.getGrayTrackInfo().getParameter(configBean.getField());

        }
        return null;
    }


}
