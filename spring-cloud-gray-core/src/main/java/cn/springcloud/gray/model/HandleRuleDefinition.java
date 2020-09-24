package cn.springcloud.gray.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-24 22:43
 */
@Data
public class HandleRuleDefinition implements Serializable {
    private static final long serialVersionUID = -1799758887400489344L;
    private String id;
    private String type;
    private Set<String> matchingPolicyIds;
    private String handleOption;
    private int order;
}
