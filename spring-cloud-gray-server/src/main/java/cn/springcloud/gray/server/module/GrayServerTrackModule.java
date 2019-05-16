package cn.springcloud.gray.server.module;

import cn.springcloud.gray.server.module.domain.GrayTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GrayServerTrackModule {


    List<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId);

    List<GrayTrack> listGrayTracksByInstanceId(String instanceId);

    Page<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId, Pageable pageable);

    Page<GrayTrack> listGrayTracksByInstanceId(String instanceId, Pageable pageable);

    void deleteGrayTrack(Long id);

    void saveGrayTrack(GrayTrack track);
}
