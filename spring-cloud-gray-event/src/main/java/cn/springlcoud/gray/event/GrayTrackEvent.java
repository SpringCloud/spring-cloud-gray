package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.GrayTrackDefinition;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 12:15
 */
@Data
public class GrayTrackEvent extends GrayEvent<GrayTrackDefinition> {

    private String serviceId;
    private String instanceId;
    private String sourceId;

    public GrayTrackEvent() {
    }


    @Override
    public String getSourceId() {
        return sourceId;
    }


}
