package cn.springcloud.gray.server.resources.domain.vo;

import cn.springcloud.gray.model.RoutePolicyType;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RoutePolicyRecordVO {
    @ApiModelProperty("id")
    private Long id;
    /**
     * 类型,如service_route, instance_route, service_multi_ver_route,
     * 参看 {@link RoutePolicyType}
     */

    @ApiModelProperty("命名空间")
    private String namespace;
    @ApiModelProperty("类型,如service_route, instance_route, service_multi_ver_route")
    private String type;
    @ApiModelProperty("模块id，用于搜索索引，如果serviceId")
    private String moduleId;
    @ApiModelProperty("路由的资源，如：serviceId, instanceId, service version")
    private String resource;
    @ApiModelProperty("策略id")
    private Long policyId;
    @ApiModelProperty("策略别名")
    private String policyAlias;
    @ApiModelProperty("是否删除")
    private Boolean delFlag;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作时间")
    private Date operateTime;


//    public static String generateInstanceRoutePolicyId(String instanceId, Long policyId) {
//        return Md5Utils.md5(instanceId + "_" + policyId);
//    }


    public static RoutePolicyRecordVO of(RoutePolicyRecord routePolicyRecord) {
        RoutePolicyRecordVO vo = new RoutePolicyRecordVO();
        vo.setDelFlag(routePolicyRecord.getDelFlag());
        vo.setId(routePolicyRecord.getId());
        vo.setModuleId(routePolicyRecord.getModuleId());
        vo.setNamespace(routePolicyRecord.getNamespace());
        vo.setOperateTime(routePolicyRecord.getOperateTime());
        vo.setOperator(routePolicyRecord.getOperator());
        vo.setPolicyId(routePolicyRecord.getPolicyId());
        vo.setResource(routePolicyRecord.getResource());
        vo.setType(routePolicyRecord.getType());
        return vo;
    }

}
