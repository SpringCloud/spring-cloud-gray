package cn.springcloud.gray.handle;

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


    @Override
    public HandleInfo removeHandleInfo(String handleId) {
        return handleInfoMap.remove(handleId);
    }

    @Override
    public HandleInfo getHandleInfo(String handleId) {
        return handleInfoMap.get(handleId);
    }

    @Override
    public void clearAllHandleInfos() {
        handleInfoMap.clear();
    }

    @Override
    public void addHandleInfo(HandleInfo handleInfo) {
        handleInfoMap.put(handleInfo.getId(), handleInfo);
    }

    @Override
    public void addHandleDefinition(HandleDefinition handleDefinition) {

    }

    @Override
    public void addHandleActionDefinition(String handleId, HandleActionDefinition handleActionDefinition) {
        HandleInfo handleInfo = getHandleInfo(handleId);
        if (Objects.isNull(handleInfo)) {
            return;
        }
        handleInfo.addHandleActionDefinition(handleActionDefinition);
    }

    @Override
    public HandleActionDefinition removeHandleActionDefinition(String handleId, String actionId) {
        HandleInfo handleInfo = getHandleInfo(handleId);
        if (Objects.isNull(handleInfo)) {
            return null;
        }
        return handleInfo.removeHandleActionDefinition(actionId);
    }
}
