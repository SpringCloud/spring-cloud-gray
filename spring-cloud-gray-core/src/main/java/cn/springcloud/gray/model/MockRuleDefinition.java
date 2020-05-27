package cn.springcloud.gray.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-22 06:56
 */
@Data
public class MockRuleDefinition implements Serializable {

    private static final long serialVersionUID = 7896350368454464042L;

    private String id;
    private Set<String> routePolicies;
    private String mockId;

}
