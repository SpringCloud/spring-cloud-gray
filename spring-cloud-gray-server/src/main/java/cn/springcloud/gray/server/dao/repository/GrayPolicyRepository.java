package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayPolicyRepository extends JpaRepository<GrayPolicyDO, Long> {
    List<GrayPolicyDO> findByInstanceId(String instanceId);

    Page<GrayPolicyDO> findAllByInstanceId(String instanceId, Pageable pageable);
}
