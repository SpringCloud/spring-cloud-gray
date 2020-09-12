package cn.springcloud.gray.commons;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.local.InstanceLocalInfo;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackRecordDevice;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;

/**
 * @author saleson
 * @date 2020-05-26 10:53
 */
public class GrayRequestHelper {

    public static final String PREVIOUS_SERVER_SERVICEID = "previous.server.serviceId";
    public static final String PREVIOUS_SERVER_INSTANCEID = "previous.server.instanceId";
    public static final String PREVIOUS_SERVER_HOST = "previous.server.host";
    public static final String PREVIOUS_SERVER_PORT = "previous.server.port";

    public static void setPreviousServerInfoByInstanceLocalInfo(GrayRequest grayRequest) {
        setPreviousServerInfoByInstanceLocalInfo(grayRequest, GrayClientHolder.getInstanceLocalInfo());
    }

    public static void setPreviousServerInfoByInstanceLocalInfo(GrayRequest grayRequest, InstanceLocalInfo instanceLocalInfo) {
        if (Objects.isNull(instanceLocalInfo)) {
            return;
        }
        grayRequest.setAttribute(GrayRequestHelper.PREVIOUS_SERVER_SERVICEID, instanceLocalInfo.getServiceId());
        grayRequest.setAttribute(GrayRequestHelper.PREVIOUS_SERVER_INSTANCEID, instanceLocalInfo.getInstanceId());
        grayRequest.setAttribute(GrayRequestHelper.PREVIOUS_SERVER_HOST, instanceLocalInfo.getHost());
        grayRequest.setAttribute(GrayRequestHelper.PREVIOUS_SERVER_PORT, String.valueOf(instanceLocalInfo.getPort()));
    }

    public static void setPreviousServerInfoToHttpHeaderByInstanceLocalInfo(GrayHttpRequest grayRequest) {
        setPreviousServerInfoToHttpHeaderByInstanceLocalInfo(grayRequest, GrayClientHolder.getInstanceLocalInfo());

    }

    public static void setPreviousServerInfoToHttpHeaderByInstanceLocalInfo(GrayHttpRequest grayRequest, InstanceLocalInfo instanceLocalInfo) {
        if (Objects.isNull(instanceLocalInfo)) {
            return;
        }
        grayRequest.addHeader(GrayRequestHelper.PREVIOUS_SERVER_SERVICEID, instanceLocalInfo.getServiceId());
        grayRequest.addHeader(GrayRequestHelper.PREVIOUS_SERVER_INSTANCEID, instanceLocalInfo.getInstanceId());
        grayRequest.addHeader(GrayRequestHelper.PREVIOUS_SERVER_HOST, instanceLocalInfo.getHost());
        grayRequest.addHeader(GrayRequestHelper.PREVIOUS_SERVER_PORT, String.valueOf(instanceLocalInfo.getPort()));
    }

    public static void recordLocalInstanceInfos(GrayTrackRecordDevice recordDevice) {
        recordLocalInstanceInfos(recordDevice, GrayClientHolder.getInstanceLocalInfo());
    }

    public static void recordLocalInstanceInfos(GrayTrackRecordDevice recordDevice, InstanceLocalInfo instanceLocalInfo) {
        if (Objects.isNull(instanceLocalInfo)) {
            return;
        }
        recordDevice.record(GrayRequestHelper.PREVIOUS_SERVER_SERVICEID, instanceLocalInfo.getServiceId());
        recordDevice.record(GrayRequestHelper.PREVIOUS_SERVER_INSTANCEID, instanceLocalInfo.getInstanceId());
        recordDevice.record(GrayRequestHelper.PREVIOUS_SERVER_HOST, instanceLocalInfo.getHost());
        recordDevice.record(GrayRequestHelper.PREVIOUS_SERVER_PORT, String.valueOf(instanceLocalInfo.getPort()));
    }


    /**
     * 将HttpServerRequest的信息设置到GrayHttpRequest中
     *
     * @param httpServletRequest
     * @param grayHttpRequest
     */
    public static void setHttpServerRequestInfoToGrayHttpRequest(HttpServletRequest httpServletRequest, GrayHttpRequest grayHttpRequest) {
        grayHttpRequest.setUri(URI.create(httpServletRequest.getRequestURI()));
        grayHttpRequest.setMethod(httpServletRequest.getMethod());

        httpServletRequest.getParameterMap().forEach((k, values) -> {
            grayHttpRequest.setParameters(k, Arrays.asList(values));
        });

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            List<String> headerValues = toList(httpServletRequest.getHeaders(headerName));
            grayHttpRequest.setHeader(headerName, headerValues);
        }
    }


    private static <T> List<T> toList(Enumeration<T> enumeration) {
        List<T> list = new ArrayList<>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }
}
