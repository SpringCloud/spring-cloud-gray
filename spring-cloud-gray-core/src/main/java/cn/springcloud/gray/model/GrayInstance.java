package cn.springcloud.gray.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 灰度实例，有状态属性
 */
@ToString
@Setter
@Getter
@JsonIgnoreProperties({"gray"})
public class GrayInstance implements Serializable {

    private static final long serialVersionUID = 1604426811546120884L;
    private String serviceId;
    private String instanceId;
    private String host;
    private Integer port;

    /**
     * 类度策略组
     */
    private List<PolicyDefinition> policyDefinitions = new CopyOnWriteArrayList<>();

    private GrayStatus grayStatus;


    public boolean isGray() {
        return grayStatus == GrayStatus.OPEN;
    }


}
