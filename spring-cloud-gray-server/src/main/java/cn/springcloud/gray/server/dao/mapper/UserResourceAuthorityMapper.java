package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.UserResourceAuthorityDO;
import cn.springcloud.gray.server.module.domain.ResourceAuthorityFlag;
import cn.springcloud.gray.server.module.user.domain.UserResourceAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class UserResourceAuthorityMapper implements ModelMapper<UserResourceAuthority, UserResourceAuthorityDO> {


    int toString(ResourceAuthorityFlag authorityFlag) {
        return authorityFlag.getFlag();
    }

    ResourceAuthorityFlag toEnum(int flag) {
        return ResourceAuthorityFlag.ofFlag(flag);
    }

}
