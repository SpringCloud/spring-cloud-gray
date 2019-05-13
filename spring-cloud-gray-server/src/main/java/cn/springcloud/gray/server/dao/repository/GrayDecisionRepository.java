package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayDecisionRepository extends JpaRepository<GrayDecisionDO, Long> {
    List<GrayDecisionDO> findByPolicyId(Long policyId);

    void deleteAllByPolicyId(Long id);
}
