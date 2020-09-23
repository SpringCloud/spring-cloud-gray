package cn.springcloud.gray.event;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-05 14:27
 */
@Data
public class GrayServiceEvent extends GrayEvent {


    private String serviceId;

    @Override
    public String getSourceId() {
        return serviceId;
    }
}
