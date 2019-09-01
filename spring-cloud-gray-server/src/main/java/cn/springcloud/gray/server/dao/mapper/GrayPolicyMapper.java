package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrayPolicyMapper extends ModelMapper<GrayPolicy, GrayPolicyDO> {

}
