package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayInstanceRepository extends JpaRepository<GrayInstanceDO, String> {
    List<GrayInstanceDO> findByServiceId(String serviceId);

    List<GrayInstanceDO> findAllByGrayStatus(String status);

    List<GrayInstanceDO> findAllByGrayStatusAndInstanceStatus(String name, String name1);

    Page<GrayInstanceDO> findAllByServiceId(String serviceId, Pageable pageable);
}
