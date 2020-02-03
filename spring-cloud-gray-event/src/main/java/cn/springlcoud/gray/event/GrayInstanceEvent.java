package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.GrayInstance;

/**
 * @author saleson
 * @date 2020-02-03 09:38
 */
public class GrayInstanceEvent extends GrayEvent<GrayInstance> {

    public GrayInstanceEvent(long sortMark, GrayInstance source) {
        super(sortMark, source);
    }

    @Override
    public String getSourceId() {
        return getSource().getInstanceId();
    }

}
