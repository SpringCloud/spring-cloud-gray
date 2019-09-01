package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.ServiceOwnerDO;
import cn.springcloud.gray.server.module.user.domain.ServiceOwner;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ServiceOwnerMapper extends ModelMapper<ServiceOwner, ServiceOwnerDO> {

}
