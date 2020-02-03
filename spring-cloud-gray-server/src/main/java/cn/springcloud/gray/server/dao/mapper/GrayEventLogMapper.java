package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.GrayEventLogDO;
import cn.springcloud.gray.server.module.gray.domain.GrayEventLog;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrayEventLogMapper extends ModelMapper<GrayEventLog, GrayEventLogDO> {

}
