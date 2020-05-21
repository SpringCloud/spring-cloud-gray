package cn.springcloud.gray.mock;

/**
 * @author saleson
 * @date 2020-05-17 15:52
 */
public interface MockAction<RESULT> {

    RESULT mock(MockInfo info);

}
