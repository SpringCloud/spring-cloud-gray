package cn.springcloud.gray.handle;

import cn.springcloud.gray.changed.notify.ChangedType;
import cn.springcloud.gray.model.HandleActionDefinition;
import cn.springcloud.gray.model.HandleDefinition;

/**
 * @author saleson
 * @date 2020-05-24 22:18
 */
public interface HandleManager {


    HandleInfo removeHandleInfo(String handleId);


    HandleInfo getHandleInfo(String handleId);


    void clearAllHandleInfos();


    void addHandleInfo(HandleInfo handleInfo);


    void addHandleDefinition(HandleDefinition handleDefinition);


    void addHandleActionDefinition(String handleId, HandleActionDefinition handleActionDefinition);


    HandleActionDefinition removeHandleActionDefinition(String handleId, String actionId);


    void publishHandleInfoChanged(ChangedType changedType, HandleInfo handleInfo);

}
