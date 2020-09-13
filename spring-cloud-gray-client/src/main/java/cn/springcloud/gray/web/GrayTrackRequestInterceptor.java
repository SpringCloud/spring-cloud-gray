package cn.springcloud.gray.web;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.request.GrayHttpRequest;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
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
        if (!(request instanceof GrayHttpRequest)) {
            return true;
        }
        GrayHttpRequest grayHttpRequest = (GrayHttpRequest) request;
        GrayTrackInfo grayTrackInfo = request.getGrayTrackInfo();
        if (grayTrackInfo != null) {
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
            GrayTrackInfo grayTrackInfo = request.getGrayTrackInfo();
            if (MapUtils.isNotEmpty(grayTrackInfo.getAttributes())) {
                grayTrackInfo.getAttributes().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    request.addHeader(name, entry.getValue());
                });
            }
        });


        handlers.add(request -> {
            GrayTrackInfo grayTrackInfo = request.getGrayTrackInfo();
            if (MapUtils.isNotEmpty(grayTrackInfo.getHeaders())) {
                Map<String, List<String>> h = request.getHeaders();
                grayTrackInfo.getHeaders().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_HEADER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

        handlers.add(request -> {
            GrayTrackInfo grayTrackInfo = request.getGrayTrackInfo();
            if (MapUtils.isNotEmpty(grayTrackInfo.getParameters())) {
                Map<String, List<String>> h = request.getHeaders();
                grayTrackInfo.getParameters().entrySet().forEach(entry -> {
                    String name = new StringBuilder().append(GrayTrackInfo.GRAY_TRACK_PARAMETER_PREFIX)
                            .append(GrayTrackInfo.GRAY_TRACK_SEPARATE)
                            .append(entry.getKey()).toString();
                    h.put(name, entry.getValue());
                });
            }
        });

    }
}
