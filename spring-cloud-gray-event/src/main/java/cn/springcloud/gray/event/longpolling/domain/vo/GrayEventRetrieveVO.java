package cn.springcloud.gray.event.longpolling.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-02-03 21:25
 */
@Data
@ApiModel
public class GrayEventRetrieveVO {

    @ApiModelProperty(value = "最大的排序号")
    private Long maxSortMark;

    @ApiModelProperty(value = "事件列表")
    private List<GrayEventVO> grayEvents = new ArrayList<>();

}
