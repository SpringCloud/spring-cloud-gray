package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.HandleActionDO;
import cn.springcloud.gray.server.module.domain.HandleAction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * @author saleson
 * @date 2020-05-31 10:04
 */
@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface HandleActionMapper extends ModelMapper<HandleAction, HandleActionDO> {
}
