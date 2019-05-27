package cn.springcloud.gray.web.tracker;

import cn.springcloud.gray.request.GrayHttpTrackInfo;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.TrackArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;

@Slf4j
public class HttpReceiveGrayInfoTracker implements HttpGrayInfoTracker {


    private Map<String, Consumer<LoadSpec>> loaders = new HashMap<>();


    public HttpReceiveGrayInfoTracker() {
        init();
    }


    public void call(GrayHttpTrackInfo trackInfo, HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName.startsWith(GrayTrackInfo.GRAY_TRACK_PREFIX)) {
                String[] names = headerName.split(GrayTrackInfo.GRAY_TRACK_SEPARATE);
                loadGrayTrackInfo(new LoadSpec(headerName, names, trackInfo, request));
            }
        }
    }


    @Override
    public void call(TrackArgs<GrayHttpTrackInfo, HttpServletRequest> args) {
        call(args.getTrackInfo(), args.getRequest());
    }

    @Override
    public int getOrder() {
        return 100;
    }

    private void loadGrayTrackInfo(LoadSpec loadSpec) {
        String[] names = loadSpec.getNames();
        Optional.ofNullable(loaders.get(names[0])).ifPresent(loader -> {
            loader.accept(loadSpec);
        });
    }

    private void init() {
        loaders.put(GrayTrackInfo.GRAY_TRACK_TRACE_IP, loadSpec -> {
            String value = loadSpec.getHeaderValue();
            loadSpec.getTrackInfo().setTraceIp(value);
            log.debug("接收到{} --> {}", GrayTrackInfo.GRAY_TRACK_TRACE_IP, value);
        });

        loaders.put(GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX, loadSpec -> {
            String value = loadSpec.getHeaderValue();
            loadSpec.getTrackInfo().setAttribute(loadSpec.getNames()[1], loadSpec.getHeaderValue());
            log.debug("接收到{} --> {}", loadSpec.getHeaderName(), value);
        });

        loaders.put(GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX, loadSpec -> {
            List<String> value = loadSpec.getHeaderValues();
            loadSpec.getTrackInfo().setHeader(loadSpec.getNames()[1], value);
            log.debug("接收到{} --> {}", loadSpec.getHeaderName(), value);
        });

        loaders.put(GrayHttpTrackInfo.GRAY_TRACK_METHOD, loadSpec -> {
            String value = loadSpec.getHeaderValue();
            loadSpec.getTrackInfo().setMethod(value);
            log.debug("接收到{} --> {}", loadSpec.getHeaderName(), value);
        });

        loaders.put(GrayHttpTrackInfo.GRAY_TRACK_URI, loadSpec -> {
            String value = loadSpec.getHeaderValue();
            loadSpec.getTrackInfo().setUri(value);
            log.debug("接收到{} --> {}", loadSpec.getHeaderName(), value);
        });

        loaders.put(GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX, loadSpec -> {
            List<String> value = loadSpec.getHeaderValues();
            loadSpec.getTrackInfo().setParameters(loadSpec.getNames()[1], value);
            log.debug("接收到{} --> {}", loadSpec.getHeaderName(), value);
        });
    }


    @Getter
    @AllArgsConstructor
    private static class LoadSpec {
        private String headerName;
        private String[] names;
        private GrayHttpTrackInfo trackInfo;
        private HttpServletRequest request;


        public String getHeaderValue() {
            return request.getHeader(headerName);
        }

        public List<String> getHeaderValues() {
            Enumeration<String> ve = request.getHeaders(headerName);
            List<String> values = new ArrayList<>();
            while (ve.hasMoreElements()) {
                values.add(ve.nextElement());
            }
            return values;
        }


    }
}
