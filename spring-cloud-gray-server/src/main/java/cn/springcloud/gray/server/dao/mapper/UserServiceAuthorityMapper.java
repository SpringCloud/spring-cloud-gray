package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.UserServiceAuthorityDO;
import cn.springcloud.gray.server.module.user.domain.UserServiceAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserServiceAuthorityMapper extends ModelMapper<UserServiceAuthority, UserServiceAuthorityDO> {

}
