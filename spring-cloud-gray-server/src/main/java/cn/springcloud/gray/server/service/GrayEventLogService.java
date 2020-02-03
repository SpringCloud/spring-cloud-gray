package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayEventLogMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayEventLogDO;
import cn.springcloud.gray.server.dao.repository.GrayEventLogRepository;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author saleson
 * @date 2020-01-31 22:53
 */
@Service
public class GrayEventLogService extends AbstraceCRUDService<GrayEventLog, GrayEventLogRepository, GrayEventLogDO, String> {

    @Autowired
    private GrayEventLogMapper mapper;
    @Autowired
    private GrayEventLogRepository repository;

    @Override
    protected GrayEventLogRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayEventLog, GrayEventLogDO> getModelMapper() {
        return mapper;
    }


}
