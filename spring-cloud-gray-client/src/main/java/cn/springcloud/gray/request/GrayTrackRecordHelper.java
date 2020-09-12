package cn.springcloud.gray.request;

import cn.springcloud.gray.commons.GrayRequestHelper;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;

public class GrayTrackRecordHelper {

    private GrayTrackRecordHelper() {
    }

    public static void recordHttpTrack(GrayTrackRecordDevice recordDevice, GrayTrackInfo grayTrackInfo) {
        GrayHttpTrackInfo httpTrackInfo = (GrayHttpTrackInfo) grayTrackInfo;

        Map<String, List<String>> trackParameters = httpTrackInfo.getParameters();
        if (MapUtils.isNotEmpty(trackParameters)) {
            recordGrayTrackInfos(recordDevice, GrayHttpTrackInfo.GRAY_TRACK_PARAMETER_PREFIX, trackParameters);
        }

        Map<String, List<String>> trackHeaders = httpTrackInfo.getHeaders();
        if (MapUtils.isNotEmpty(trackHeaders)) {
            recordGrayTrackInfos(recordDevice, GrayHttpTrackInfo.GRAY_TRACK_HEADER_PREFIX, trackHeaders);
        }

        recordTrack(recordDevice, grayTrackInfo);
        GrayRequestHelper.recordLocalInstanceInfos(recordDevice);
    }

    public static void recordTrack(GrayTrackRecordDevice recordDevice, GrayTrackInfo grayTrackInfo) {
        Map<String, String> grayAttributes = grayTrackInfo.getAttributes();
        if (MapUtils.isNotEmpty(grayAttributes)) {
            recordGrayTrackInfo(recordDevice, GrayTrackInfo.GRAY_TRACK_ATTRIBUTE_PREFIX, grayAttributes);
        }

        GrayRequestHelper.recordLocalInstanceInfos(recordDevice);
    }


    private static void recordGrayTrackInfo(
            GrayTrackRecordDevice recordDevice, String grayPrefix, Map<String, String> infos) {
        String prefix = grayPrefix + GrayTrackInfo.GRAY_TRACK_SEPARATE;
        if (MapUtils.isNotEmpty(infos)) {
            infos.entrySet().forEach(entry -> {
                recordDevice.record(prefix + entry.getKey(), entry.getValue());
            });
        }
    }

    private static void recordGrayTrackInfos(
            GrayTrackRecordDevice recordDevice, String grayPrefix, Map<String, List<String>> infos) {
        String prefix = grayPrefix + GrayTrackInfo.GRAY_TRACK_SEPARATE;
        infos.entrySet().forEach(entry -> {
            recordDevice.record(prefix + entry.getKey(), entry.getValue());
        });
    }


}
