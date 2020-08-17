package cn.springcloud.gray.dynamiclogic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2019-12-26 08:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DynamicLogicDefinition {

    private String language;
    private String name;
    private String code;
}
