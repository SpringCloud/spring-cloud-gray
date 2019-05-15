package cn.springcloud.gray.model;

import lombok.Data;
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
public class DecisionDefinition implements Serializable {

    private String id;
    private String name;
    private Map<String, String> infos = new HashMap<>();


}
