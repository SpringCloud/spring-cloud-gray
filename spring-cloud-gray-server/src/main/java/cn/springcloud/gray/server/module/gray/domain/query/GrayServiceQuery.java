package cn.springcloud.gray.server.module.gray.domain.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-03-16 23:55
 */
@Data
@AllArgsConstructor
@Builder
public class GrayServiceQuery {
    private String userId;
    private String namespace;
}
