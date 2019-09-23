package cn.springcloud.gray.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 灰度策略
 */
@ToString
@Setter
@Getter
public class DecisionDefinition implements Serializable {

    private static final long serialVersionUID = 7613293834300650748L;
    private String id;
    private String name;
    private Map<String, String> infos = new HashMap<>();


}
