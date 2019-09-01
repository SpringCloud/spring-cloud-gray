package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.mapper.ServiceOwnerMapper;
import cn.springcloud.gray.server.dao.mapper.UserServiceAuthorityMapper;
import cn.springcloud.gray.server.dao.model.ServiceOwnerDO;
import cn.springcloud.gray.server.dao.model.UserServiceAuthorityDO;
import cn.springcloud.gray.server.dao.repository.ServiceOwnerRepository;
import cn.springcloud.gray.server.dao.repository.UserServiceAuthorityRepository;
import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceAuthorityService extends AbstraceCRUDService<UserServiceAuthority, UserServiceAuthorityRepository, UserServiceAuthorityDO, Long> {

    @Autowired
    private UserServiceAuthorityRepository repository;
    @Autowired
    private UserServiceAuthorityMapper mapper;

    @Override
    protected UserServiceAuthorityRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<UserServiceAuthority, UserServiceAuthorityDO> getModelMapper() {
        return mapper;
    }


    public UserServiceAuthority findByServiceIdAndUserId(String serviceId, String userId){
        return do2model(repository.findByServiceIdAndUserId(serviceId, userId));
    }


    public Page<UserServiceAuthority> listAllServiceAuthorities(String userId, Pageable pageable) {
        return PaginationUtils.convert(pageable, repository.findAllByUserId(userId, pageable), getModelMapper());
    }

    public void deleteServiceAuthorities(String serviceId) {
        getRepository().deleteAllByServiceId(serviceId);
    }

    public Page<UserServiceAuthority> listServiceAuthorities(String serviceId, Pageable pageable) {
        return PaginationUtils.convert(pageable, getRepository().findAllByServiceId(serviceId, pageable), getModelMapper());
    }

}
