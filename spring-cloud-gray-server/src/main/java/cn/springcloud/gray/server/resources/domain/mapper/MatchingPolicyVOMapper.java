package cn.springcloud.gray.server.resources.domain.mapper;

import cn.springcloud.gray.server.module.gray.GrayPolicyModule;
import cn.springcloud.gray.server.module.gray.domain.GrayPolicy;
import cn.springcloud.gray.server.resources.domain.vo.MatchingPolicyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 01:02
 */
@Component
public class MatchingPolicyVOMapper {

    @Autowired
    private GrayPolicyModule grayPolicyModule;

    public List<MatchingPolicyVO> toMatchingPolicyVOs(Long[] policyIds) {
        List<MatchingPolicyVO> vos = new ArrayList<>(policyIds.length);
        for (Long policyId : policyIds) {
            vos.add(toMatchingPolicyVO(policyId));
        }
        return vos;
    }

    public MatchingPolicyVO toMatchingPolicyVO(Long policyId) {
        MatchingPolicyVO vo = new MatchingPolicyVO();
        vo.setPolicyId(policyId);
        GrayPolicy grayPolicy = grayPolicyModule.getGrayPolicy(policyId);
        String policyName = Objects.nonNull(grayPolicy) ? grayPolicy.getAlias() : "NoData:" + policyId;
        vo.setPolicyName(policyName);
        return vo;
    }
}
