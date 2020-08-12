package cn.springcloud.gray.server.resources.domain.mapper;

import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.resources.domain.vo.HandleRuleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author saleson
 * @date 2020-05-31 00:53
 */
@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class HandleRuleVOMapper implements VOMapper<HandleRule, HandleRuleVO> {

    @Autowired
    protected MatchingPolicyVOMapper matchingPolicyVOMapper;
    @Autowired
    protected HandleRuleVOMapHelper handleRuleVOMapHelper;

    @Override
    @Mapping(expression = "java(matchingPolicyVOMapper.toMatchingPolicyVOs(handleRule.getMatchingPolicyIds()))", target = "matchingPolicys")
    @Mapping(expression = "java(handleRuleVOMapHelper.parseHandleOptionAlias(handleRule.getHandleOption()))", target = "handleOptionAlias")
    public abstract HandleRuleVO toVO(HandleRule handleRule);


//    private List<MatchingPolicyVO> toMatchingPolicyVOs(Long[] policyIds) {
//        return matchingPolicyVOMapper.toMatchingPolicyVOs(policyIds);
//    }


}
