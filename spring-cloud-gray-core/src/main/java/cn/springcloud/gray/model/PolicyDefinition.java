package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * 灰度策略，包含一组(0,n)决策元素
 */
@Setter
@Getter
public class PolicyDefinition {

    private String policyId;
    private String alias;
    private List<DecisionDefinition> list = new ArrayList<>();

}
