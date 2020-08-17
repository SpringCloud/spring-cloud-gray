package cn.springcloud.gray.mock;

/**
 * @author saleson
 * @date 2020-05-18 07:17
 */
public interface MockHandle<RESULT> {

    RESULT mock(MockInfo info);
}
