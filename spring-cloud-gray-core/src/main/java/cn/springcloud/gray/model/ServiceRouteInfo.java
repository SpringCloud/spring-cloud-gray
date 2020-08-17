package cn.springcloud.gray.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-05-11 08:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRouteInfo {
    private String serviceId;
    private Set<String> routePolicies;
    private Map<String, Set<String>> multiVersionRoutePolicies;
}
