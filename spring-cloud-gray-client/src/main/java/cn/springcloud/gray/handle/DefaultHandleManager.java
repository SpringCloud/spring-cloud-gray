package cn.springcloud.gray.handle;

import cn.springcloud.gray.changed.notify.ChangedNotifyDriver;
import cn.springcloud.gray.changed.notify.ChangedType;
import cn.springcloud.gray.model.HandleActionDefinition;
import cn.springcloud.gray.model.HandleDefinition;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-05-26 00:43
 */
public class DefaultHandleManager implements HandleManager {

    private Map<String, HandleInfo> handleInfoMap = new ConcurrentHashMap<>();
    private ChangedNotifyDriver notifyDriver;

    public DefaultHandleManager(ChangedNotifyDriver notifyDriver) {
        this.notifyDriver = notifyDriver;
    }

    @Override
    public HandleInfo removeHandleInfo(String handleId) {
        HandleInfo old = handleInfoMap.remove(handleId);
        if (Objects.nonNull(old)) {
            publishHandleInfoChanged(ChangedType.DELETED, old);
        }
        return old;
    }

    @Override
    public HandleInfo getHandleInfo(String handleId) {
        return handleInfoMap.get(handleId);
    }

    @Override
    public void clearAllHandleInfos() {
        notifyDriver.clearAll(HandleInfo.class);
        handleInfoMap.clear();
    }

    @Override
    public void addHandleInfo(HandleInfo handleInfo) {
        HandleInfo old = handleInfoMap.put(handleInfo.getId(), handleInfo);
        publishHandleInfoChanged(Objects.isNull(old) ? ChangedType.ADDED : ChangedType.UPDATED, handleInfo);
    }

    @Override
    public void addHandleDefinition(HandleDefinition handleDefinition) {
        HandleInfo handleInfo = new HandleInfo(handleDefinition.getId(), handleDefinition.getType());
        handleDefinition.getHandleActionDefinitions().forEach(handleInfo::addHandleActionDefinition);
        addHandleInfo(handleInfo);
    }

    @Override
    public void addHandleActionDefinition(String handleId, HandleActionDefinition handleActionDefinition) {
        HandleInfo handleInfo = getHandleInfo(handleId);
        if (Objects.isNull(handleInfo)) {
            return;
        }
        handleInfo.addHandleActionDefinition(handleActionDefinition);
        publishHandleInfoChanged(ChangedType.UPDATED, handleInfo);
    }

    @Override
    public HandleActionDefinition removeHandleActionDefinition(String handleId, String actionId) {
        HandleInfo handleInfo = getHandleInfo(handleId);
        if (Objects.isNull(handleInfo)) {
            return null;
        }
        HandleActionDefinition old = handleInfo.removeHandleActionDefinition(actionId);
        publishHandleInfoChanged(ChangedType.UPDATED, handleInfo);
        return old;
    }

    @Override
    public void publishHandleInfoChanged(ChangedType changedType, HandleInfo handleInfo) {
        notifyDriver.chaned(changedType, handleInfo);
    }
}
