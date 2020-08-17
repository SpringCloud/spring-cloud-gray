package cn.springcloud.gray.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-17 17:13
 */
public class DefaultMockActionChain<RESULT> implements MockAction<RESULT> {


    private List<MockAction<RESULT>> mockActions = new ArrayList<>();

    public DefaultMockActionChain(List<MockAction<RESULT>> mockActions) {
        this.mockActions.addAll(mockActions);
    }

    @Override
    public RESULT mock(MockInfo mockInfo) {
        MockAction<RESULT> action;
        for (int i = 0; Objects.nonNull(action = getMockAction(i)); i++) {
            RESULT result = action.mock(mockInfo);
            if (Objects.nonNull(result)) {
                return result;
            }
        }
        return null;
    }

    private MockAction<RESULT> getMockAction(int actionIndex) {
        return actionIndex < mockActions.size() ? mockActions.get(actionIndex) : null;
    }

}
