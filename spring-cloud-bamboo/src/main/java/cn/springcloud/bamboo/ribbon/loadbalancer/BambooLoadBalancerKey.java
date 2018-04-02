package cn.springcloud.bamboo.ribbon.loadbalancer;

/**
 * Created by saleson on 2017/11/9.
 */
public class BambooLoadBalancerKey {


    private String serviceId;
    private String apiVersion;


    private BambooLoadBalancerKey() {

    }


    public String getApiVersion() {
        return apiVersion;
    }

    public String getServiceId() {
        return serviceId;
    }

    private void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private BambooLoadBalancerKey build = new BambooLoadBalancerKey();

        public Builder apiVersion(String apiVersion) {
            build.apiVersion = apiVersion;
            return this;
        }

        public Builder serviceId(String serviceId) {
            build.serviceId = serviceId;
            return this;
        }

        public BambooLoadBalancerKey build() {
            return build;
        }

    }
}
