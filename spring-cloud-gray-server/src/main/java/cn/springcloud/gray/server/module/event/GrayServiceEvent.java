package cn.springcloud.gray.server.module.event;

import cn.springcloud.gray.server.constant.DataOPType;
import cn.springcloud.gray.server.module.gray.domain.GrayService;

/**
 * @author saleson
 * @date 2020-09-18 00:52
 */
public class GrayServiceEvent extends GrayDataOPEvent<GrayService> {

    private static final long serialVersionUID = -8276741172406289157L;


    public GrayServiceEvent(DataOPType dataOPType, GrayService grayService) {
        super(dataOPType, grayService);
    }
}
