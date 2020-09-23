package cn.springcloud.gray.event;

import cn.springcloud.gray.model.GrayInstance;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 09:38
 */
@Data
public class GrayInstanceEvent extends GrayEvent {

    private GrayInstance source;

    @Override
    public String getSourceId() {
        return getSource().getInstanceId();
    }

}
