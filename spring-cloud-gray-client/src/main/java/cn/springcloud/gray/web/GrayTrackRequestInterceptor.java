package cn.springcloud.gray.web;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.client.config.properties.GrayTrackProperties;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;

public class GrayTrackRequestInterceptor implements RequestInterceptor {

    private GrayTrackProperties grayTrackProperties;
    private Map<String, Consumer<GrayHttpRequest>> handlers = new HashMap<>();


    public GrayTrackRequestInterceptor(GrayTrackProperties grayTrackProperties) {
        this.grayTrackProperties = grayTrackProperties;
        initHandlers();
    }

    @Override
    public String interceptroType() {
        return "all";
    }

    @Override
    public boolean shouldIntercept() {
        return true;
    }

    @Override
    public boolean pre(GrayRequest request) {
        grayTrackProperties.getWeb().getNeed().keySet().forEach(k -> {
            Optional.ofNullable(handlers.get(k)).ifPresent(h -> h.accept((GrayHttpRequest) request));
        });
        return true;
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }

    @Override
    public int getOrder() {
        return 100;
    }

    private void initHandlers() {
        handlers.put(GrayTrackProperties.Web.NEED_IP, request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getTraceIp())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, Arrays.asList(grayHttpTrackInfo.getTraceIp()));
            }
        });

        handlers.put(GrayTrackProperties.Web.NEED_URI, request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_URI, Arrays.asList(grayHttpTrackInfo.getUri()));
            }
        });


        handlers.put(GrayTrackProperties.Web.NEED_METHOD, request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_METHOD, Arrays.asList(grayHttpTrackInfo.getMethod()));
            }
        });

        handlers.put(GrayTrackProperties.Web.NEED_HEADERS, request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                grayHttpTrackInfo.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuffer().append(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

        handlers.put(GrayTrackProperties.Web.NEED_PARAMETERS, request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                grayHttpTrackInfo.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuffer().append(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

    }
}
