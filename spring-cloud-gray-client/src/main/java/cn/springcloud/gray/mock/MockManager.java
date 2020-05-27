package cn.springcloud.gray.mock;

import cn.springcloud.gray.request.GrayRequest;

/**
 * @author saleson
 * @date 2020-05-22 06:55
 */
public interface MockManager {


    boolean isEnableMock();

    boolean isEnableMock(String mockRuleType);


    <RESULT> MockHandle<RESULT> getMockHandle(String mockHandleId);


    void invalidateCache(String id);


    void invalidateAllCache();


    String predicateMockHandleId(String mockRuleType);

    String predicateMockHandleId(String mockRuleType, GrayRequest grayRequest);


    <RESULT> RESULT invokeMockHandle(String mockHandleId);

    <RESULT> RESULT invokeMockHandle(String mockHandleId, GrayReuqestMockInfo mockInfo);

    <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType);


    <RESULT> RESULT predicateAndExcuteMockHandle(String mockRuleType, GrayReuqestMockInfo mockInfo);


}
