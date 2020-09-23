package cn.springcloud.gray.event;

/**
 * @author saleson
 * @date 2020-01-30 15:32
 */
public class TGrayInstanceEvent extends GrayEvent {
    public TGrayInstanceEvent(long timestamp, Object source) {
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
