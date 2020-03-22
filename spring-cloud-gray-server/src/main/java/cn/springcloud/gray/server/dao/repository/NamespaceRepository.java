package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.NamespaceDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface NamespaceRepository extends JpaRepository<NamespaceDO, String>, JpaSpecificationExecutor<NamespaceDO> {

}
