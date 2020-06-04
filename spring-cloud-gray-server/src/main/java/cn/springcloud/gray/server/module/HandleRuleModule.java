package cn.springcloud.gray.server.module;

import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.domain.query.HandleRuleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-30 22:24
 */
public interface HandleRuleModule {


    Page<HandleRule> queryHandleRules(HandleRuleQuery handleRuleQuery, Pageable pageable);

    List<HandleRule> findHandleRules(HandleRuleQuery handleRuleQuery);

    HandleRule saveHandleRule(HandleRule handleRule);

    boolean deleteHandleRule(Long handleRuleId, String userId);

    boolean recoverHandleRule(Long handleRuleId, String userId);

    HandleRule getHandleRule(Long id);

}
