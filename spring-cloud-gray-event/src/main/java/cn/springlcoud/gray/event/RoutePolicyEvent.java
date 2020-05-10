package cn.springlcoud.gray.event;

import cn.springcloud.gray.model.RoutePolicy;
import lombok.Data;

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
}
