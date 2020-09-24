package cn.springcloud.gray.server.module.gray;

import cn.springcloud.gray.server.module.gray.domain.GrayTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GrayServerTrackModule {


    GrayTrack findFirstGrayTrack(String serviceId, String name);

    Page<GrayTrack> listGrayTracks(String serviceId, Pageable pageable);

    Page<GrayTrack> listGrayTracks(Pageable pageable);

    List<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId);

    List<GrayTrack> listGrayTracksByInstanceId(String instanceId);

    Page<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId, Pageable pageable);

    Page<GrayTrack> listGrayTracksByInstanceId(String instanceId, Pageable pageable);

    void deleteGrayTrack(Long id);

    GrayTrack getGrayTrack(Long id);

    GrayTrack saveGrayTrack(GrayTrack track);
}
