package cn.springcloud.gray.event;

import cn.springcloud.gray.model.DecisionDefinition;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 灰度策略
 */
@Setter
@Getter
public class DecisionDefinitionMsg extends DecisionDefinition implements Serializable {

    private static final long serialVersionUID = 7613293834300650748L;

    private String policyId;


}
