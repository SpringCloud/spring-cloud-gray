package cn.springcloud.gray.client.netflix.hystrix;

import cn.springcloud.gray.request.BaseRequestLocalStorage;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.LocalStorageLifeCycle;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

public class HystrixRequestLocalStorage extends BaseRequestLocalStorage {


    private static final HystrixRequestVariableDefault<GrayTrackInfo> grayTrackInfoLocal = new HystrixRequestVariableDefault<GrayTrackInfo>();
    private static final HystrixRequestVariableDefault<GrayRequest> grayRequestLocal = new HystrixRequestVariableDefault<GrayRequest>();


    public HystrixRequestLocalStorage() {
        this(new HystrixLocalStorageCycle());
    }

    public HystrixRequestLocalStorage(LocalStorageLifeCycle localStorageLifeCycle) {
        super(localStorageLifeCycle);
    }


    @Override
    public void setGrayTrackInfo(GrayTrackInfo grayTrackInfo) {
        grayTrackInfoLocal.set(grayTrackInfo);
    }

    @Override
    public void removeGrayTrackInfo() {
        grayTrackInfoLocal.remove();
    }

    @Override
    public GrayTrackInfo getGrayTrackInfo() {
        return grayTrackInfoLocal.get();
    }

    @Override
    public void setGrayRequest(GrayRequest grayRequest) {
        grayRequestLocal.set(grayRequest);
    }

    @Override
    public void removeGrayRequest() {
        grayRequestLocal.remove();
    }

    @Override
    public GrayRequest getGrayRequest() {
        return grayRequestLocal.get();
    }

}
