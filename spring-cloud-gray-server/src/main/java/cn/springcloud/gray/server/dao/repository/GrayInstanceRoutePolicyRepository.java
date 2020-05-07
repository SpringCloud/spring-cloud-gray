package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayInstanceRoutePolicyDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface GrayInstanceRoutePolicyRepository extends JpaRepository<GrayInstanceRoutePolicyDO, String>, JpaSpecificationExecutor<GrayInstanceRoutePolicyDO> {

    GrayInstanceRoutePolicyDO findFirstByInstanceIdAndPolicyId(String instanceId, Long policyId);
}
