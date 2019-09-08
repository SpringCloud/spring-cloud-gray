package cn.springcloud.gray.local;


public class DefaultInstanceLocalInfoInitiralizer extends LazyInstanceLocalInfoInitiralizer {

    @Override
    protected String getLocalInstanceId() {
        return "";
    }
}
