package cn.springcloud.gray.event;

import cn.springcloud.gray.model.RoutePolicy;
import lombok.Data;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-07 11:44
 */
@Data
public class RoutePolicyEvent extends GrayEvent {

    private RoutePolicy routePolicy;


    @Override
    public String getSourceId() {
        return String.valueOf(routePolicy.getId());
    }

    public String getRoutePolicyType() {
        return Objects.isNull(routePolicy) ? null : routePolicy.getType();
    }
}
