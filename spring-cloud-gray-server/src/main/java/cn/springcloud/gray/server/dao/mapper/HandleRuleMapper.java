package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.HandleRuleDO;
import cn.springcloud.gray.server.module.domain.HandleRule;
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
public interface HandleRuleMapper extends ModelMapper<HandleRule, HandleRuleDO> {
}
