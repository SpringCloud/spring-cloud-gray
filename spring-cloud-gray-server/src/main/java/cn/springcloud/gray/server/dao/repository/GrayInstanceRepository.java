package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayInstanceRepository extends JpaRepository<GrayInstanceDO, String> {
    List<GrayInstanceDO> findByServiceId(String serviceId);

    List<GrayInstance> findAllByGrayStatus(String status);
}
