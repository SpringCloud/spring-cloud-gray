package cn.springlcoud.gray.event.longpolling.domain.vo;

import cn.springlcoud.gray.event.GrayEvent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 21:27
 */
@ApiModel
@Data
public class GrayEventVO {
    @ApiModelProperty(value = "事件对象类型", required = true)
    private String eventClass;
    @ApiModelProperty(value = "事件对象", required = true)
    private GrayEvent event;

}
