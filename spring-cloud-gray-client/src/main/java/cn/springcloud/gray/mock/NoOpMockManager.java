package cn.springcloud.gray.mock;

import cn.springcloud.gray.request.GrayRequest;

/**
 * @author saleson
 * @date 2020-05-27 18:40
 */
public class NoOpMockManager implements MockManager {
    @Override
    public boolean isEnableMock() {
        return false;
    }

    @Override
    public boolean isEnableMock(String mockRuleType) {
        return false;
    }

    @Override
    public <RESULT> MockHandle<RESULT> getMockHandle(String mockHandleId) {
        return null;
    }

    @Override
    public void invalidateCache(String id) {

    }

    @Override
    public void invalidateAllCache() {

    }

    @Override
    public String predicateMockHandleId(String mockRuleType) {
        return null;
    }

    @Override
    public String predicateMockHandleId(String mockRuleType, GrayRequest grayRequest) {
        return null;
    }

    @Override
    public <RESULT> RESULT invokeMockHandle(String mockHandleId) {
        return null;
    }

    @Override
    public <RESULT> RESULT invokeMockHandle(String mockHandleId, GrayReuqestMockInfo mockInfo) {
        return null;
    }

    @Override
    public <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType) {
        return null;
    }

    @Override
    public <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType, GrayReuqestMockInfo mockInfo) {
        return null;
    }
}
