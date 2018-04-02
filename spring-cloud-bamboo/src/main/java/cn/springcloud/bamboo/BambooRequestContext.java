package cn.springcloud.bamboo;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BambooRequestContext {

    private static final Logger log = LoggerFactory.getLogger(BambooRequestContext.class);

    private static final HystrixRequestVariableDefault<BambooRequestContext> CURRENT_CONTEXT = new HystrixRequestVariableDefault<BambooRequestContext>();


    private final String apiVersion;
    private final BambooRequest bambooRequest;
    private Map<String, Object> params;


    private BambooRequestContext(BambooRequest bambooRequest, String apiVersion) {
        params = new HashMap<>();
        this.apiVersion = apiVersion;
        this.bambooRequest = bambooRequest;
    }


    public static BambooRequestContext currentRequestCentxt() {
        return CURRENT_CONTEXT.get();
    }

    public static void initRequestContext(BambooRequest bambooRequest, String apiVersion) {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
        CURRENT_CONTEXT.set(new BambooRequestContext(bambooRequest, apiVersion));
    }

    public static void shutdownRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }


    public String getApiVersion() {
        return apiVersion;
    }

    public String getServiceId() {
        return bambooRequest.getServiceId();
    }


    public void addParameter(String key, Object value){
        params.put(key, value);
    }

    public Object getParameter(String key){
        return params.get(key);
    }


    public String getStrParameter(String key){
        return (String) params.get(key);
    }

    public Integer getIntegerParameter(String key){
        return (Integer) params.get(key);
    }


    public Long getLongParameter(String key){
        return (Long) params.get(key);
    }

    public Boolean getBooleanParameter(String key){
        return (Boolean) params.get(key);
    }

    public BambooRequest getBambooRequest() {
        return bambooRequest;
    }
}
