package cn.springcloud.gray.mock;

import cn.springcloud.gray.Pair;
import cn.springcloud.gray.mock.factory.MockActionFactory;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-18 13:50
 */
public interface MockDriver {

    <RESULT> MockAction<RESULT> createMockAction(String action, Object definition);


    <RESULT> List<MockAction<RESULT>> createMockActions(List<Pair<String, Object>> definitions);


    <RESULT> MockHandle<RESULT> createMockHandle(List<Pair<String, Object>> definitions);


    <RESULT> List<MockAction<RESULT>> createMockActions(Pair<String, Object>... definitions);


    <RESULT> MockHandle<RESULT> createMockHandle(Pair<String, Object>... definitions);


    void addMockActionFactory(MockActionFactory mockActionFactory);

    MockActionFactory removeMockActionFactory(String actionName);

}
