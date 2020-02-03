package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayEventLogMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayEventLogDO;
import cn.springcloud.gray.server.dao.repository.GrayEventLogRepository;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<GrayEventLog> queryAllGreaterThanSortMark(long sortMark) {
        return dos2models(repository.queryAllBySortMarkGreaterThanOrderBySortMark(sortMark));
    }

    public long getNewestSortMark() {
        Pageable pageable = new PageRequest(0, 1);
        List<Long> newestSortMarks = repository.queryNewestSortMarks(pageable);
        return newestSortMarks.size() > 0 ? newestSortMarks.get(0) : 0;
    }
}
