package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.ServiceOwnerDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceOwnerRepository extends JpaRepository<ServiceOwnerDO, String>, JpaSpecificationExecutor<ServiceOwnerDO> {


    ServiceOwnerDO findByServiceId(String serviceId);
}
