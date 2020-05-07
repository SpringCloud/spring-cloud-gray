package cn.springlcoud.gray.event;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-07 11:44
 */
@Data
public class GrayInstanceRoutePolicyEvent extends GrayEvent {

    private Long instanceRoutePolicyId;
    private String instanceId;
    private String serviceId;
    private String policyId;


    @Override

    public String getSourceId() {
        return String.valueOf(instanceRoutePolicyId);
    }
}
