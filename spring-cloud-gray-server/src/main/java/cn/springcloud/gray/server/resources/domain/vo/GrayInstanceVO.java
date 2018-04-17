package cn.springcloud.gray.server.resources.domain.vo;

import java.util.Map;

public class GrayInstanceVO {

    private String serviceId;
    private String instanceId;
    private String appName;
    private String url;
    private Map<String, String> metadata;
    private boolean hasGrayPolicies;
    private boolean openGray;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean isHasGrayPolicies() {
        return hasGrayPolicies;
    }

    public void setHasGrayPolicies(boolean hasGrayPolicies) {
        this.hasGrayPolicies = hasGrayPolicies;
    }

    public boolean isOpenGray() {
        return openGray;
    }

    public void setOpenGray(boolean openGray) {
        this.openGray = openGray;
    }
}
