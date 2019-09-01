package cn.springcloud.gray.decision.factory;

import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public abstract class AbstractAssignFieldGrayDecisionFactory<C extends AbstractAssignFieldGrayDecisionFactory.AssignFieldConfig> extends AbstractGrayDecisionFactory<C> {


    public static final String FIELD_SCOPE_REQUEST_ATTRIBUTE = "attribute";
    public static final String FIELD_SCOPE_TRACK_ATTRIBUTE = "TrackAttribute";

    private static Map<String, BiFunction<GrayHttpRequest, String, ?>> fieldValueGetters = new HashMap<>();

    protected AbstractAssignFieldGrayDecisionFactory(Class<C> configClass) {
        super(configClass);
    }


    protected Object getRequestAssignFieldValue(GrayRequest grayRequest, C configBean){
        switch (configBean.getType()){
            case FIELD_SCOPE_REQUEST_ATTRIBUTE:
                return grayRequest.getAttribute(configBean.getField());
            case FIELD_SCOPE_TRACK_ATTRIBUTE:
                GrayTrackInfo trackInfo = grayRequest.getGrayTrackInfo();
                if(trackInfo==null){
                    return null;
                }
                return trackInfo.getAttribute(configBean.getField());
        }
        return null;
    }


    protected String getRequestAssignFieldStringValue(GrayRequest grayRequest, C configBean){
        return convertToString(getRequestAssignFieldValue(grayRequest, configBean));
    }






    protected String convertToString(Object fieldValue){
        if(Objects.isNull(fieldValue)){
            return null;
        }
        if(fieldValue instanceof String){
            return (String) fieldValue;
        }
        if(fieldValue instanceof Collection){
            return joinString((Collection<String>) fieldValue);
        }
        return fieldValue.toString();
    }


    private String joinString(Collection<String> collection) {
        if (collection != null) {
            return StringUtils.join(collection, ",");
        }
        return null;
    }

    @Setter
    @Getter
    public static class AssignFieldConfig {
        private String type;
        private String field;

    }
}
