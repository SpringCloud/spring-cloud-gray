package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.GrayServiceMapper;
import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.model.GrayServiceDO;
import cn.springcloud.gray.server.dao.repository.GrayServiceRepository;
import cn.springcloud.gray.server.module.domain.GrayService;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GrayServiceService extends AbstraceCRUDService<GrayService, GrayServiceRepository, GrayServiceDO, String> {

    @Autowired
    private GrayServiceRepository repository;
    @Autowired
    private GrayInstanceService grayInstanceService;
    @Autowired
    private GrayServiceMapper grayServiceMapper;


    @Override
    protected GrayServiceRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<GrayService, GrayServiceDO> getModelMapper() {
        return grayServiceMapper;
    }

    public void deleteById(String id) {
        delete(id);
        grayInstanceService.findByServiceId(id);
    }

    public void deleteReactById(String id) {
        delete(id);
        grayInstanceService.findByServiceId(id).forEach(entity -> {
            grayInstanceService.deleteReactById(entity.getInstanceId());
        });
    }


    public Page<GrayService> listAllGrayServices(Pageable pageable) {
        Page<GrayServiceDO> entities = repository.findAll(pageable);
        return PaginationUtils.convert(pageable, entities, grayServiceMapper);
    }
}
