package cn.springcloud.gray.mock;

import java.util.List;

/**
 * @author saleson
 * @date 2020-05-20 23:26
 */
public class DefaultMockHandle<RESULT> implements MockHandle<RESULT> {

    private List<MockAction<RESULT>> mockActions;

    public DefaultMockHandle(List<MockAction<RESULT>> mockActions) {
        this.mockActions = mockActions;
    }

    @Override
    public RESULT mock(MockInfo info) {
        MockAction<RESULT> chain = new DefaultMockActionChain<>(mockActions);
        return chain.mock(info);
    }
}
