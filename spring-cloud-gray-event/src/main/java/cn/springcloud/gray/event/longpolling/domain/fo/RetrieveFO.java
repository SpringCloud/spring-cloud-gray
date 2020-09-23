package cn.springcloud.gray.event.longpolling.domain.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author saleson
 * @date 2020-02-03 21:36
 */
@ApiModel
@Data
public class RetrieveFO {
    @NotNull(message = "排序标记不能为空")
    @ApiModelProperty(value = "排序标记，类似于时间戳", required = true)
//    @Min(value = 1000000000000l, message = "排序标记不符合格式要求")
//    @Max(value = 10000000000000l, message = "排序标记不符合格式要求")
    private Long sortMark;
    @NotEmpty(message = "实例id不能为空")
    @ApiModelProperty(value = "实例id", required = true)
    private String instanceId;
    @NotEmpty(message = "服务id不能为空")
    @ApiModelProperty(value = "服务id", required = true)
    private String serviceId;

}
