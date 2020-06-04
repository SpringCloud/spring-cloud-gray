package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.HandleActionDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandleActionRepository extends JpaRepository<HandleActionDO, Long> {

    List<HandleActionDO> findAllByHandleId(Long handleId);

    List<HandleActionDO> findAllByHandleIdAndDelFlag(Long handleId, boolean delFlag);


    Page<HandleActionDO> findAllByHandleId(Long handleId, Pageable pageable);

}
