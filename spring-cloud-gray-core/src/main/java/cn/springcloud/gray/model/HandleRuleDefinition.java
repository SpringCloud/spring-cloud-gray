package cn.springcloud.gray.model;

import lombok.Data;

import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-24 22:43
 */
@Data
public class HandleRuleDefinition {
    private String id;
    private String type;
    private Set<String> policyIds;
    private String handleInfo;
    private int order;
}
