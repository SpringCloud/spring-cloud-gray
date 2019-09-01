package cn.springcloud.gray.server.module.user;

import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import cn.springcloud.gray.server.module.user.domain.ServiceOwnerQuery;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceManageModule {

    boolean hasServiceAuthority(String serviceId, String userId);

    boolean isServiceOwner(String serviceId, String userId);

    boolean hasServiceAuthority(String serviceId);

    Page<UserServiceAuthority> listAllUserServiceAuthorities(Pageable pageable);

    Page<UserServiceAuthority> listServiceAuthorities(String serviceId, Pageable pageable);

    Page<String> listAllUserServiceIds(Pageable pageable);

    Page<UserServiceAuthority> listAllServiceAuthorities(String userId, Pageable pageable);

    Page<String> listAllServiceIds(String userId, Pageable pageable);

    ServiceOwner addServiceOwner(String serviceId);

    ServiceOwner insertServiceOwner(String serviceId);

    ServiceOwner addServiceOwner(String serviceId, String userId);

    ServiceOwner transferServiceOwner(String serviceId, String userId);

    ServiceOwner getServiceOwner(String serviceId);

    Page<ServiceOwner> queryServiceOwners(ServiceOwnerQuery query, Pageable pageable);

    UserServiceAuthority getServiceAuthority(Long id);


    void deleteSericeManeges(String serviceId);

    void deleteServiceOwner(String serviceId);

    void deleteServiceAuthorities(String serviceId);

    void deleteServiceAuthority(Long id);

    UserServiceAuthority addServiceAuthority(String serviceId, String userId);
}
