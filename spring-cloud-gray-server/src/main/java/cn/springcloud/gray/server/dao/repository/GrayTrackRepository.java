package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayTrackDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayTrackRepository extends JpaRepository<GrayTrackDO, Long> {


    Page<GrayTrackDO> findAllByInstanceId(String instanceId, Pageable pageable);

    List<GrayTrackDO> findAllByInstanceId(String instanceId);

    @Query(value = "SELECT do FROM GrayTrackDO do WHERE serviceId = ?1 AND (instanceId = null or instanceId='') ")
    List<GrayTrackDO> findAllByServiceIdAndInstanceIdIsEmpty(String serviceId);


    @Query(value = "SELECT do FROM GrayTrackDO do WHERE serviceId = ?1 AND (instanceId = null or instanceId='') ")
    Page<GrayTrackDO> findAllByServiceIdAndInstanceIdIsEmpty(String instanceId, Pageable pageable);
}
