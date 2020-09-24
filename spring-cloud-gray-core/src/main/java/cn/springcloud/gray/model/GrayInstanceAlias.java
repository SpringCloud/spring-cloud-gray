package cn.springcloud.gray.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author saleson
 * @date 2020-09-09 00:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayInstanceAlias implements Serializable {
    private static final long serialVersionUID = 8433446869828423603L;
    private String serviceId;
    private String instanceId;
    private String[] aliases;
}
