package cn.springcloud.gray.server.module.audit;

import cn.springcloud.gray.server.module.audit.domain.OperateQuery;
import cn.springcloud.gray.server.module.audit.domain.OperateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OperateAuditModule {

    void recordOperate(OperateRecord record);

    Page<OperateRecord> queryRecords(OperateQuery query, Pageable pageable);
}
