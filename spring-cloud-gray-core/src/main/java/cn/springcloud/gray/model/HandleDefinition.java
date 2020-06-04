package cn.springcloud.gray.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-22 22:59
 */
@Data
public class HandleDefinition implements Serializable {
    private static final long serialVersionUID = -2073221418460049108L;


    private String id;
    private String name;

    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    private String type;
    private Set<HandleActionDefinition> handleActionDefinitions;
}
