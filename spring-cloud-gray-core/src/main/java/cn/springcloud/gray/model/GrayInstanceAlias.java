package cn.springcloud.gray.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-09-09 00:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayInstanceAlias {
    private String serviceId;
    private String instanceId;
    private String[] aliases;
}
