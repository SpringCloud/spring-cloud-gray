package cn.springcloud.gray.event;

/**
 * @author saleson
 * @date 2020-09-01 00:01
 */
public class EmptyGrayEvent extends GrayEvent {
    private static final long serialVersionUID = -6870842690230609456L;

    @Override
    public String getSourceId() {
        return "";
    }
}
