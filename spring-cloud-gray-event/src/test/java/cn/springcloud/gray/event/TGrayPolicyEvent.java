package cn.springcloud.gray.event;

/**
 * @author saleson
 * @date 2020-01-30 21:40
 */
public class TGrayPolicyEvent extends GrayEvent {
    public TGrayPolicyEvent(long timestamp, Object source) {
    }

    @Override
    public String getSourceId() {
        return "";
    }

    @Override
    public String getType() {
        return "";
    }
}
