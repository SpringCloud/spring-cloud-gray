package cn.springcloud.bamboo.ribbon;

import cn.springcloud.bamboo.ribbon.loadbalancer.BambooLoadBalancerKey;
import com.netflix.client.ClientRequest;

/**
 * Created by saleson on 2017/12/21.
 */
public class BambooRequestContext {

    private static final ThreadLocal<BambooRequestContext> local = new ThreadLocal<>();


    static void currentContext(BambooRequestContext context) {
        local.set(context);
    }

    public static BambooRequestContext instance() {
        return local.get();
    }

    private static void removeCurrentContext() {
        local.remove();
    }


    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private BambooLoadBalancerKey loadBalancerKey;
        private ClientRequest request;

        private Builder() {
        }

        public Builder loadBalancerKey(BambooLoadBalancerKey loadBalancerKey) {
            this.loadBalancerKey = loadBalancerKey;
            return this;
        }

        public Builder request(ClientRequest request) {
            this.request = request;
            return this;
        }

        public BambooRequestContext build() {
            return new BambooRequestContext(loadBalancerKey, request);
        }
    }


    private final BambooLoadBalancerKey loadBalancerKey;
    private final ClientRequest request;

    private BambooRequestContext(BambooLoadBalancerKey loadBalancerKey, ClientRequest request) {
        this.loadBalancerKey = loadBalancerKey;
        this.request = request;
    }

    public BambooLoadBalancerKey getLoadBalancerKey() {
        return loadBalancerKey;
    }

    public ClientRequest getRequest() {
        return request;
    }

    public void toThreadLocal() {
        currentContext(this);
    }

    public void removeByThreadLocal() {
        if (instance() == this) {
            removeCurrentContext();
        }
    }
}
