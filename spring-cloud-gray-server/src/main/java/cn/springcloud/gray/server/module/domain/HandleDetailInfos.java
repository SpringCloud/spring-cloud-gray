package cn.springcloud.gray.server.module.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author saleson
 * @date 2020-03-17 12:15
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class HandleDetailInfos {
    @ApiModelProperty("处理描述")
    private Handle handle;
    @ApiModelProperty("处理动作")
    private List<HandleAction> handleActions;
}
