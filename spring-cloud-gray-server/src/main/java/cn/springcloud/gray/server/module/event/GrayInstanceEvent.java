package cn.springcloud.gray.server.module.event;

import cn.springcloud.gray.server.constant.DataOPType;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;

/**
 * @author saleson
 * @date 2020-09-18 00:52
 */
public class GrayInstanceEvent extends GrayDataOPEvent<GrayInstance> {

    private static final long serialVersionUID = -8276741172406289157L;


    public GrayInstanceEvent(DataOPType dataOPType, GrayInstance grayInstance) {
        super(dataOPType, grayInstance);
    }
}
