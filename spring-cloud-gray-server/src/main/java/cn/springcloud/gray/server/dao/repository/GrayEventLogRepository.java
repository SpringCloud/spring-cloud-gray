package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.GrayEventLogDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrayEventLogRepository extends JpaRepository<GrayEventLogDO, String> {

//    @Query(value = "select GrayEventLogDO.sortMark from GrayEventLogDO order by GrayEventLogDO.sortMark desc")
//    List<Long> queryNewestSortMark(Pageable pageable);

    List<GrayEventLogDO> queryAllBySortMarkGreaterThanOrderBySortMark(long sortMark);
}
