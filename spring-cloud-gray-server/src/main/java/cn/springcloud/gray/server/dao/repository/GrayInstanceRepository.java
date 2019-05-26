package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GrayInstanceRepository extends JpaRepository<GrayInstanceDO, String> {
    List<GrayInstanceDO> findByServiceId(String serviceId);

    List<GrayInstanceDO> findAllByGrayStatus(String grayStatus);

    List<GrayInstanceDO> findAllByGrayStatusAndInstanceStatus(String grayStatus, String instanceStatus);

    Page<GrayInstanceDO> findAllByServiceId(String serviceId, Pageable pageable);

    List<GrayInstanceDO> findAllByServiceIdAndInstanceStatusIn(String serviceId, String[] instanceStatus);

    List<GrayInstanceDO> findAllByGrayStatusAndInstanceStatusIn(String grayStatus, String[] instanceStatus);


    List<GrayInstanceDO> findAllByLastUpdateDateBeforeAndInstanceStatusIn(Date lastUpdateDate, String[] instanceStatus);
}
