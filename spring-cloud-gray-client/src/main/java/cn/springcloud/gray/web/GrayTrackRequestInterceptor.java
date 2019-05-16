package cn.springcloud.gray.web;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GrayTrackRequestInterceptor implements RequestInterceptor {

    private List<Consumer<GrayHttpRequest>> handlers = new ArrayList<>();


    public GrayTrackRequestInterceptor() {
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
        GrayHttpRequest grayHttpRequest = (GrayHttpRequest) request;
        GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
        if (grayHttpTrackInfo != null) {
            handlers.forEach(h -> {
                h.accept(grayHttpRequest);
            });
        }
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
        handlers.add(request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getTraceIp())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_TRACE_IP, Arrays.asList(grayHttpTrackInfo.getTraceIp()));
            }
        });

        handlers.add(request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_URI, Arrays.asList(grayHttpTrackInfo.getUri()));
            }
        });


        handlers.add(request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                h.put(GrayHttpTrackInfo.GRAY_TRACK_METHOD, Arrays.asList(grayHttpTrackInfo.getMethod()));
            }
        });

        handlers.add(request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                grayHttpTrackInfo.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

        handlers.add(request -> {
            GrayHttpTrackInfo grayHttpTrackInfo = (GrayHttpTrackInfo) request.getGrayTrackInfo();
            if (StringUtils.isNotEmpty(grayHttpTrackInfo.getUri())) {
                Map<String, List<String>> h = (Map<String, List<String>>) request.getHeaders();
                grayHttpTrackInfo.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

    }
}
