package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.OperateRecordDO;
import cn.springcloud.gray.server.dao.model.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface OperateRecordRepository extends JpaRepository<OperateRecordDO, Long>, JpaSpecificationExecutor<OperateRecordDO> {


}
