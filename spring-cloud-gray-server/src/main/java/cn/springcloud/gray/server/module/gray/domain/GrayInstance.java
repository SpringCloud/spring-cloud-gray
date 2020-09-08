package cn.springcloud.gray.server.module.gray.domain;

import cn.springcloud.gray.model.GrayStatus;
import cn.springcloud.gray.model.InstanceStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;


@ApiModel("实例的灰度信息")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrayInstance {

    public static final int GRAY_LOCKED = 1;

    public static final int GRAY_UNLOCKED = 0;


    @ApiModelProperty("服务实例id")
    private String instanceId;
    @ApiModelProperty("服务id")
    private String serviceId;
    private String host;
    @ApiModelProperty("服务实例端口")
    private Integer port;
    @ApiModelProperty("描述")
    private String des;
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
    @ApiModelProperty("操作时间")
    private Date operateTime;
    @ApiModelProperty("操作人")
    private String operator;

    /**
     * 实例状态
     */
    @ApiModelProperty("服务实例状态")
    private InstanceStatus instanceStatus;


    @ApiModelProperty("别名")
    private String[] aliases;
    /**
     * 灰度状态
     */
    @ApiModelProperty("灰度状态")
    private GrayStatus grayStatus;

    @ApiModelProperty("灰度锁定")
    private Integer grayLock = GRAY_UNLOCKED;

}
