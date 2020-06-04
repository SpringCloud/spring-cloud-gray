package cn.springcloud.gray.server.module.domain;

import cn.springcloud.gray.model.HandleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-05-30 22:27
 */
@Data
@ApiModel
public class Handle {


    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("处理名称")
    private String name;

    @ApiModelProperty("命名空间")
    private String namespace;
    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    @ApiModelProperty("处理类型: 比如 mock")
    private String type;

    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;

}
