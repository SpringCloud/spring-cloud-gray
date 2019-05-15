package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.GrayInstanceDO;
import cn.springcloud.gray.server.dao.model.GrayServiceDO;
import cn.springcloud.gray.server.module.domain.GrayInstance;
import cn.springcloud.gray.server.module.domain.GrayService;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrayInstanceMapper extends ModelMapper<GrayInstance, GrayInstanceDO> {

}
