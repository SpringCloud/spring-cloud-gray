package cn.springcloud.gray.server.module.jpa;

import cn.springcloud.gray.server.module.HandleRuleModule;
import cn.springcloud.gray.server.module.domain.HandleRule;
import cn.springcloud.gray.server.module.domain.query.HandleRuleQuery;
import cn.springcloud.gray.server.service.HandleRuleService;
import cn.springcloud.gray.event.server.GrayEventTrigger;
import cn.springcloud.gray.event.server.TriggerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-31 10:01
 */
public class JPAHandleRuleModule implements HandleRuleModule {

    private HandleRuleService handleRuleService;
    private GrayEventTrigger grayEventTrigger;

    public JPAHandleRuleModule(HandleRuleService handleRuleService, GrayEventTrigger grayEventTrigger) {
        this.handleRuleService = handleRuleService;
        this.grayEventTrigger = grayEventTrigger;
    }

    @Override
    public Page<HandleRule> queryHandleRules(HandleRuleQuery handleRuleQuery, Pageable pageable) {
        return handleRuleService.findAllModels(handleRuleQuery, pageable);
    }

    @Override
    public List<HandleRule> findHandleRules(HandleRuleQuery handleRuleQuery) {
        return handleRuleService.findAllModels(handleRuleQuery);
    }

    @Override
    public HandleRule saveHandleRule(HandleRule handleRule) {
        boolean triggerEvent = false;
        HandleRule old = null;
        if (Objects.isNull(handleRule.getId())) {
            if (Objects.isNull(handleRule.getDelFlag())) {
                handleRule.setDelFlag(false);
            }
            triggerEvent = true;
        } else {
            old = getHandleRule(handleRule.getId());
            if (Objects.isNull(old) || Objects.equals(old.getDelFlag(), Boolean.FALSE)) {
                triggerEvent = true;
            }
        }
        if (Objects.isNull(handleRule.getOperateTime())) {
            handleRule.setOperateTime(new Date());
        }

        handleRule = handleRuleService.saveModel(handleRule);
        if (triggerEvent) {
            triggerGrayEvent(Objects.isNull(old) ? TriggerType.ADD : TriggerType.MODIFY, handleRule);
        }
        return handleRule;
    }

    @Override
    public boolean deleteHandleRule(Long handleRuleId, String userId) {
        int result = updateHandleRuleDelFlag(handleRuleId, true, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.DELETE, handleRuleService.findOneModel(handleRuleId));
        }
        return result < 0;
    }

    @Override
    public boolean recoverHandleRule(Long handleRuleId, String userId) {
        int result = updateHandleRuleDelFlag(handleRuleId, false, userId);
        if (result == 2) {
            triggerGrayEvent(TriggerType.ADD, handleRuleService.findOneModel(handleRuleId));
        }
        return result < 0;
    }


    protected int updateHandleRuleDelFlag(Long handleActionId, Boolean newDelFlag, String userId) {
        HandleRule record = handleRuleService.findOneModel(handleActionId);
        if (Objects.isNull(record)) {
            return 0;
        }
        if (Objects.equals(record.getDelFlag(), newDelFlag)) {
            return 1;
        }
        record.setDelFlag(newDelFlag);
        record.setOperateTime(new Date());
        record.setOperator(userId);
        handleRuleService.saveModel(record);
        return 2;
    }

    @Override
    public HandleRule getHandleRule(Long id) {
        return handleRuleService.findOneModel(id);
    }

    protected void triggerGrayEvent(TriggerType triggerType, Object source) {
        grayEventTrigger.triggering(source, triggerType);
    }
}
