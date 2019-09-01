package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.UserServiceAuthorityDO;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserServiceAuthorityRepository extends JpaRepository<UserServiceAuthorityDO, Long> {


    UserServiceAuthorityDO findByServiceIdAndUserId(String serviceId, String userId);

    Page<UserServiceAuthorityDO> findAllByUserId(String userId, Pageable pageable);

    int deleteAllByServiceId(String serviceId);

    Page<UserServiceAuthorityDO> findAllByServiceId(String serviceId, Pageable pageable);
}
