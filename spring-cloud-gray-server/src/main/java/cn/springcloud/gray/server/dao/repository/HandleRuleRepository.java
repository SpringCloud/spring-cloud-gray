package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.HandleRuleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HandleRuleRepository extends JpaRepository<HandleRuleDO, Long>, JpaSpecificationExecutor<HandleRuleDO> {


}
