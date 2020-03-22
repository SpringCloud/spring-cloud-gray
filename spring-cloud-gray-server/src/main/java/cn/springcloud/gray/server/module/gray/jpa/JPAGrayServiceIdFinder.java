package cn.springcloud.gray.server.module.gray.jpa;

import cn.springcloud.gray.server.module.gray.GrayServiceIdFinder;
import cn.springcloud.gray.server.module.gray.domain.GrayInstance;
import cn.springcloud.gray.server.module.gray.domain.GrayModelType;
import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import cn.springcloud.gray.server.service.GrayInstanceService;
import cn.springcloud.gray.server.service.GrayTrackService;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JPAGrayServiceIdFinder implements GrayServiceIdFinder {

    private GrayInstanceService grayInstanceService;
    private GrayTrackService grayTrackService;

    private Map<GrayModelType, Function<Object, String>> getFunctions = new HashMap<>();


    public JPAGrayServiceIdFinder(
            GrayInstanceService grayInstanceService,
            GrayTrackService grayTrackService) {
        this.grayInstanceService = grayInstanceService;
        this.grayTrackService = grayTrackService;
        init();
    }

    public void init() {
        getFunctions.put(GrayModelType.TRACK, this::findByTrack);
        getFunctions.put(GrayModelType.INSTANCE, this::findByInstance);
    }


    @Override
    public String getServiceId(GrayModelType grayModelType, Object id) {
        Function<Object, String> function = getFunctions.get(grayModelType);
        if (function == null) {
            return "-1";
        }
        return StringUtils.defaultIfEmpty(function.apply(id), "-1");
    }

    private String findByTrack(Object id) {
        GrayTrack grayTrack = grayTrackService.findOneModel((Long) id);
        return grayTrack == null ? null : grayTrack.getServiceId();
    }

    private String findByInstance(Object id) {
        GrayInstance grayInstance = grayInstanceService.findOneModel((String) id);
        return grayInstance == null ? null : grayInstance.getServiceId();
    }

}
