package cn.springcloud.gray.server.resources.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GrayServiceVO {

    @ApiModelProperty("服务名")
    private String appName;

    @ApiModelProperty("服务id")
    private String serviceId;

    @ApiModelProperty("服务实例数")
    private int instanceSize;


    @ApiModelProperty("是否拥有灰度实例")
    private boolean hasGrayInstances;

    @ApiModelProperty("是否拥有灰度策略")
    private boolean hasGrayPolicies;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getInstanceSize() {
        return instanceSize;
    }

    public void setInstanceSize(int instanceSize) {
        this.instanceSize = instanceSize;
    }

    public boolean isHasGrayInstances() {
        return hasGrayInstances;
    }

    public void setHasGrayInstances(boolean hasGrayInstances) {
        this.hasGrayInstances = hasGrayInstances;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isHasGrayPolicies() {
        return hasGrayPolicies;
    }

    public void setHasGrayPolicies(boolean hasGrayPolicies) {
        this.hasGrayPolicies = hasGrayPolicies;
    }
}
