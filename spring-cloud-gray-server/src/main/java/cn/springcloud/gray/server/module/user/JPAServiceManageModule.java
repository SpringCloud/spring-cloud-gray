package cn.springcloud.gray.server.module.user;

import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import cn.springcloud.gray.server.module.user.domain.ServiceOwnerQuery;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import cn.springcloud.gray.server.service.ServiceOwnerService;
import cn.springcloud.gray.server.service.UserServiceAuthorityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JPAServiceManageModule implements ServiceManageModule {

    private UserModule userModule;
    private UserServiceAuthorityService userServiceAuthorityService;
    private ServiceOwnerService serviceOwnerService;

    public JPAServiceManageModule(
            UserModule userModule,
            UserServiceAuthorityService userServiceAuthorityService,
            ServiceOwnerService serviceOwnerService) {
        this.userModule = userModule;
        this.userServiceAuthorityService = userServiceAuthorityService;
        this.serviceOwnerService = serviceOwnerService;
    }

    @Override
    public boolean hasServiceAuthority(String serviceId, String userId) {
        return userServiceAuthorityService.findByServiceIdAndUserId(serviceId, userId) != null;
    }

    @Override
    public boolean isServiceOwner(String serviceId, String userId) {
        ServiceOwner serviceOwner = getServiceOwner(serviceId);
        return serviceOwner == null ? false : StringUtils.equals(serviceOwner.getUserId(), userId);
    }

    @Override
    public boolean hasServiceAuthority(String serviceId) {
        String userId = userModule.getCurrentUserId();
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        return hasServiceAuthority(serviceId, userId);
    }

    @Override
    public Page<UserServiceAuthority> listAllUserServiceAuthorities(Pageable pageable) {
        return userServiceAuthorityService.findAllModels(pageable);
    }

    @Override
    public Page<UserServiceAuthority> listServiceAuthorities(String serviceId, Pageable pageable) {
        return userServiceAuthorityService.listServiceAuthorities(serviceId, pageable);
    }

    @Override
    public Page<String> listAllUserServiceIds(Pageable pageable) {
        Page<UserServiceAuthority> entities = listAllUserServiceAuthorities(pageable);
        List<String> serviceIds = entities.getContent().stream().map(UserServiceAuthority::getServiceId).collect(Collectors.toList());
        return new PageImpl<>(serviceIds, pageable, entities.getTotalElements());
    }

    @Override
    public Page<UserServiceAuthority> listAllServiceAuthorities(String userId, Pageable pageable) {
        return userServiceAuthorityService.listAllServiceAuthorities(userId, pageable);
    }

    @Override
    public Page<String> listAllServiceIds(String userId, Pageable pageable) {
        Page<UserServiceAuthority> entities = listAllServiceAuthorities(userId, pageable);
        List<String> serviceIds = entities.getContent().stream().map(UserServiceAuthority::getServiceId).collect(Collectors.toList());
        return new PageImpl<>(serviceIds, pageable, entities.getTotalElements());
    }

    @Transactional
    @Override
    public ServiceOwner addServiceOwner(String serviceId) {
        return addServiceOwner(serviceId, userModule.getCurrentUserId());
    }

    @Override
    public ServiceOwner insertServiceOwner(String serviceId) {
        ServiceOwner serviceOwner = new ServiceOwner();
        serviceOwner.setServiceId(serviceId);
        return serviceOwnerService.saveModel(serviceOwner);
    }

    @Transactional
    @Override
    public ServiceOwner addServiceOwner(String serviceId, String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        ServiceOwner serviceOwner = new ServiceOwner();
        serviceOwner.setServiceId(serviceId);
        serviceOwner.setUserId(userId);
        serviceOwnerService.saveModel(serviceOwner);
        addServiceAuthority(serviceId, userId);
        return null;
    }

    @Transactional
    @Override
    public ServiceOwner transferServiceOwner(String serviceId, String userId) {
        ServiceOwner serviceOwner = new ServiceOwner();
        serviceOwner.setServiceId(serviceId);
        serviceOwner.setUserId(userId);
        serviceOwner.setOperator(userModule.getCurrentUserId());
        serviceOwner.setOperateTime(new Date());
        serviceOwnerService.saveModel(serviceOwner);
        if(!hasServiceAuthority(serviceId, userId)) {
            addServiceAuthority(serviceId, userId);
        }
        return serviceOwner;
    }

    @Override
    public ServiceOwner getServiceOwner(String serviceId) {
        return serviceOwnerService.findOneModel(serviceId);
    }

    @Override
    public Page<ServiceOwner> queryServiceOwners(ServiceOwnerQuery query, Pageable pageable) {
        return serviceOwnerService.queryServiceOwners(query, pageable);
    }

    @Override
    public UserServiceAuthority getServiceAuthority(Long id) {
        return userServiceAuthorityService.findOneModel(id);
    }

    @Transactional
    @Override
    public void deleteSericeManeges(String serviceId) {
        deleteServiceOwner(serviceId);
        deleteServiceAuthorities(serviceId);
    }

    @Override
    public void deleteServiceOwner(String serviceId) {
        serviceOwnerService.delete(serviceId);
    }

    @Override
    public void deleteServiceAuthorities(String serviceId) {
        userServiceAuthorityService.deleteServiceAuthorities(serviceId);
    }

    @Override
    public void deleteServiceAuthority(Long id) {
        userServiceAuthorityService.delete(id);
    }

    @Override
    public UserServiceAuthority addServiceAuthority(String serviceId, String userId) {
        UserServiceAuthority userServiceAuthority = new UserServiceAuthority();
        userServiceAuthority.setServiceId(serviceId);
        userServiceAuthority.setUserId(userId);
        userServiceAuthority.setOperator(userModule.getCurrentUserId());
        userServiceAuthority.setOperateTime(new Date());
        return userServiceAuthorityService.saveModel(userServiceAuthority);
    }
}
