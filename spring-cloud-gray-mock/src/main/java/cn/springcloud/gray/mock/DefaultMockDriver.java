package cn.springcloud.gray.mock;

import cn.springcloud.gray.Pair;
import cn.springcloud.gray.mock.factory.MockActionFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-05-20 23:37
 */
public class DefaultMockDriver implements MockDriver {

    private Map<String, MockActionFactory> mockActionFactorys = new ConcurrentHashMap<>();


    public DefaultMockDriver() {

    }

    public DefaultMockDriver(List<MockActionFactory> mockActionFactories) {
        mockActionFactories.forEach(this::addMockActionFactory);
    }


    @Override
    public <RESULT> MockAction<RESULT> createMockAction(String action, Object definition) {
        MockActionFactory actionFactory = getMockActionFactory(action);
        if (Objects.isNull(actionFactory)) {
            return null;
        }
        return actionFactory.create(definition);
    }

    @Override
    public <RESULT> List<MockAction<RESULT>> createMockActions(List<Pair<String, Object>> definitions) {
        List<MockAction<RESULT>> mockActions = new ArrayList<>(definitions.size());
        definitions.forEach(definition -> {
            MockAction<RESULT> mockAction = createMockAction(definition.getKey(), definition.getValue());
            if (Objects.nonNull(mockAction)) {
                mockActions.add(mockAction);
            }
        });
        return mockActions;
    }

    @Override
    public <RESULT> MockHandle<RESULT> createMockHandle(List<Pair<String, Object>> definitions) {
        List<MockAction<RESULT>> mockActions = createMockActions(definitions);
        return new DefaultMockHandle<>(mockActions);
    }

    @Override
    public <RESULT> List<MockAction<RESULT>> createMockActions(Pair<String, Object>... definitions) {
        return createMockActions(Arrays.asList(definitions));
    }

    @Override
    public <RESULT> MockHandle<RESULT> createMockHandle(Pair<String, Object>... definitions) {
        return createMockHandle(Arrays.asList(definitions));
    }

    @Override
    public void addMockActionFactory(MockActionFactory mockActionFactory) {
        mockActionFactorys.put(mockActionFactory.name(), mockActionFactory);
    }

    @Override
    public MockActionFactory removeMockActionFactory(String actionName) {
        return mockActionFactorys.remove(actionName);
    }


    protected MockActionFactory getMockActionFactory(String action) {
        return mockActionFactorys.get(action);
    }
}
