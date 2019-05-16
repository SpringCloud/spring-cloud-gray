package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayTrackMapper;
import cn.springcloud.gray.server.dao.model.GrayTrackDO;
import cn.springcloud.gray.server.dao.repository.GrayTrackRepository;
import cn.springcloud.gray.server.module.domain.GrayTrack;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrayTrackService extends AbstraceCRUDService<GrayTrack, GrayTrackRepository, GrayTrackDO, Long> {

    @Autowired
    private GrayTrackRepository repository;
    @Autowired
    private GrayTrackMapper grayTrackMapper;

    @Override
    protected GrayTrackRepository getRepository() {
        return repository;
    }

    @Override
    protected GrayTrackMapper getModelMapper() {
        return grayTrackMapper;
    }

    public List<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId) {
        return dos2models(repository.findAllByServiceIdAndInstanceIdIsEmpty(serviceId));
    }

    public List<GrayTrack> listGrayTracksByInstanceId(String instanceId) {
        return dos2models(repository.findAllByInstanceId(instanceId));
    }

    public Page<GrayTrack> listGrayTracksEmptyInstanceByServiceId(String serviceId, Pageable pageable) {
        Page<GrayTrackDO> page = repository.findAllByServiceIdAndInstanceIdIsEmpty(serviceId, pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());
    }

    public Page<GrayTrack> listGrayTracksByInstanceId(String instanceId, Pageable pageable) {
        Page<GrayTrackDO> page = repository.findAllByInstanceId(instanceId, pageable);
        return PaginationUtils.convert(pageable, page, getModelMapper());

    }
}
