package cn.springcloud.gray.mock;

/**
 * @author saleson
 * @date 2020-05-25 17:00
 */
public interface ResponseMockDevice<INPUT, OUTPUT> {

    OUTPUT mock(INPUT input);

}
