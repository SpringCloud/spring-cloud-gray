package cn.springcloud.gray.decision;


import cn.springcloud.bamboo.BambooRequest;
import cn.springcloud.gray.core.GrayDecision;

import java.util.List;

public class RequestIpDecision implements GrayDecision {

    public static final String IPS_KEY = "ips";

    private final List<String> ips;

    public RequestIpDecision(List<String> ips) {
        if (ips == null || ips.isEmpty()) {
            throw new NullPointerException("ips must not be empty");
        }
        this.ips = ips;
    }

    @Override
    public boolean test(BambooRequest bambooRequest) {
        return ips.contains(bambooRequest.getIp());
    }
}
