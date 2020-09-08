package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.GrayInstanceAlias;

/**
 * @author saleson
 * @date 2020-09-09 00:18
 */
public class GrayInstanceAliasEvent extends GrayEvent {

    private static final long serialVersionUID = -758204380752290963L;
    private GrayInstanceAlias source;

    public GrayInstanceAliasEvent(GrayInstanceAlias source) {
        this.source = source;
    }

    @Override
    public String getSourceId() {
        return getSource().getInstanceId();
    }

    public GrayInstanceAlias getSource() {
        return source;
    }
}
