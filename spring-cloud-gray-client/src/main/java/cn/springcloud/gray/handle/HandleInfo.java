package cn.springcloud.gray.handle;

import cn.springcloud.gray.model.HandleActionDefinition;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-05-24 22:29
 */
public class HandleInfo {

    private final String id;
    private String type;
    private Map<String, HandleActionDefinition> handleActionDefinitions = new ConcurrentHashMap();

    public HandleInfo(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public void addHandleActionDefinition(HandleActionDefinition definition) {
        handleActionDefinitions.put(definition.getId(), definition);
    }

    public HandleActionDefinition removeHandleActionDefinition(String id) {
        return handleActionDefinitions.remove(id);
    }


    public List<HandleActionDefinition> getHandleActionDefinitions() {
        List<HandleActionDefinition> mockActionDefinitions = this.handleActionDefinitions.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        return mockActionDefinitions;
    }


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, HandleActionDefinition> getHandleActionDefinitionMap() {
        return Collections.unmodifiableMap(handleActionDefinitions);
    }
}
