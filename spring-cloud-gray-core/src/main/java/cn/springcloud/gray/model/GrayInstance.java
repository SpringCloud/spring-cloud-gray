package cn.springcloud.gray.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;


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
    private String[] aliases;

    /**
     * 类度策略组
     * 兼容第一个版本，下一大版本将会弃用
     */
    @Deprecated
    private List<PolicyDefinition> policyDefinitions = new CopyOnWriteArrayList<>();

    private Set<String> routePolicies;

    private GrayStatus grayStatus;


    public boolean isGray() {
        return grayStatus == GrayStatus.OPEN;
    }


    public void addRoutePolicy(String policyId) {
        routePolicies.add(policyId);
    }

    public void removeRoutePolicy(String policyId) {
        routePolicies.remove(policyId);
    }


    public static GrayInstance copyof(GrayInstance other) {
        GrayInstance bean = new GrayInstance();
        bean.setPort(other.getPort());
        bean.setHost(other.getHost());
        bean.setGrayStatus(other.getGrayStatus());
        bean.setInstanceId(other.getInstanceId());
        bean.setServiceId(other.getServiceId());
        bean.setAliases(other.getAliases());
        bean.setRoutePolicies(new CopyOnWriteArraySet<>());
        if (Objects.nonNull(other.getRoutePolicies())) {
            bean.getRoutePolicies().addAll(other.getRoutePolicies());
        }
        return bean;
    }

}
