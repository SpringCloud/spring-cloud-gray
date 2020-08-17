package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.AuthorityDO;
import cn.springcloud.gray.server.module.user.domain.AuthorityDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AuthorityMapper extends ModelMapper<AuthorityDetail, AuthorityDO> {

}
