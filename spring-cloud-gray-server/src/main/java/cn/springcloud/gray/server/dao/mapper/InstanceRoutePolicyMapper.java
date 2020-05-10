package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.RoutePolicyRecordDO;
import cn.springcloud.gray.server.module.route.policy.domain.RoutePolicyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface InstanceRoutePolicyMapper extends ModelMapper<RoutePolicyRecord, RoutePolicyRecordDO> {

}
