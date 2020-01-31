package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayEventLogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrayEventLogRepository extends JpaRepository<GrayEventLogDO, String> {
}
