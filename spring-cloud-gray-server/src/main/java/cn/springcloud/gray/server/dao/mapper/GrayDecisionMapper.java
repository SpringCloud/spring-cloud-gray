package cn.springcloud.gray.server.dao.mapper;

import cn.springcloud.gray.server.dao.model.GrayDecisionDO;
import cn.springcloud.gray.server.dao.model.GrayPolicyDO;
import cn.springcloud.gray.server.module.domain.GrayDecision;
import cn.springcloud.gray.server.module.domain.GrayPolicy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrayDecisionMapper extends ModelMapper<GrayDecision, GrayDecisionDO> {

}
