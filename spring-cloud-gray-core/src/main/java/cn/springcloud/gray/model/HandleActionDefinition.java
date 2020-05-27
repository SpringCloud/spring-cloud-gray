package cn.springcloud.gray.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-22 06:57
 */
@Data
public class HandleActionDefinition implements Serializable, Comparable<HandleActionDefinition> {

    private static final long serialVersionUID = -853332256929060345L;

    private String id;
    private String actionName;
    private Map<String, String> infos = new HashMap<>();
    private int order;


    @Override
    public int compareTo(HandleActionDefinition o) {
        if (Objects.isNull(o)) {
            return -1;
        }
        return Integer.compare(this.getOrder(), o.getOrder());
    }

}
