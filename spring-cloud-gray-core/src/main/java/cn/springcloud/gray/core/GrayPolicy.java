package cn.springcloud.gray.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 灰度策略
 */
public class GrayPolicy {

    private String policyId;
    private String policyType;
    private Map<String, String> infos = new HashMap<>();


    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public void addInfo(String key, String value) {
        infos.put(key, value);
    }


    public Map<String, String> getInfos() {
        return infos;
    }

    public void setInfos(Map<String, String> infos) {
        this.infos = new HashMap<>(infos);
    }
}
