package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayDecisionRepository extends JpaRepository<GrayDecisionDO, Long>, JpaSpecificationExecutor<GrayDecisionDO> {
    List<GrayDecisionDO> findByPolicyIdAndDelFlag(Long policyId, boolean delFlag);

    void deleteAllByPolicyId(Long id);

    Page<GrayDecisionDO> findAllByPolicyId(Long policyId, Pageable pageable);
}
