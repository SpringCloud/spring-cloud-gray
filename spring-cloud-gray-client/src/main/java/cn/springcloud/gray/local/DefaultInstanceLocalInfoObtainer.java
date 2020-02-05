package cn.springcloud.gray.local;


public class DefaultInstanceLocalInfoObtainer extends LazyInstanceLocalInfoObtainer {

    @Override
    protected String getLocalInstanceId() {
        return "";
    }
}
