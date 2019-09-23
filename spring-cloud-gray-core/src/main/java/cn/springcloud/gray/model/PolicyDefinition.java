package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 灰度策略，包含一组(0,n)决策元素
 */
@ToString
@Setter
@Getter
public class PolicyDefinition implements Serializable {

    private static final long serialVersionUID = -1l;

    private String policyId;
    private String alias;
    private List<DecisionDefinition> list = new CopyOnWriteArrayList<>();

}
