package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.HandleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HandleRepository extends JpaRepository<HandleDO, Long>, JpaSpecificationExecutor<HandleDO> {


}
