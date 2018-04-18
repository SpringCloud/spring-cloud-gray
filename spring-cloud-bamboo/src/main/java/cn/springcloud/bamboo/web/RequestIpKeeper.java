package cn.springcloud.bamboo.web;

import cn.springcloud.bamboo.BambooAppContext;
import org.apache.commons.lang3.StringUtils;

public final class RequestIpKeeper {

    private static final ThreadLocal<String> ipLocal = new ThreadLocal<>();


    private static RequestIpKeeper INSTANCE = new RequestIpKeeper();

    private RequestIpKeeper() {

    }

    public static RequestIpKeeper instance() {
        return INSTANCE;
    }


    void setIp(String ip) {
        ipLocal.set(ip);
    }


    public String getIp() {
        return ipLocal.get();
    }


    public void clear() {
        ipLocal.remove();
    }


    public static String getRequestIp() {
        String ip = instance().getIp();
        if (StringUtils.isEmpty(ip)) {
            ip = BambooAppContext.getLocalIp();
        }
        return ip;
    }

}
