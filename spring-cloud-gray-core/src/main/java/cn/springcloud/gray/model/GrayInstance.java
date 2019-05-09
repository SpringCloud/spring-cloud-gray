package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 灰度实例，有状态属性
 */
@Setter
@Getter
public class GrayInstance implements Serializable {

    private String serviceId;
    private String instanceId;

    /**
     * 类度策略组
     */
    private List<PolicyDefinition> policyDefinitions = new ArrayList<>();

    private GrayStatus grayStatus;


    public boolean isGray(){
        return grayStatus == GrayStatus.OPEN;
    }

}
