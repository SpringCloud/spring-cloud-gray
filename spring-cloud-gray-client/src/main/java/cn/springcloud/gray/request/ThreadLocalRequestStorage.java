package cn.springcloud.gray.request;

public class ThreadLocalRequestStorage extends BaseRequestLocalStorage {
    private ThreadLocal<GrayRequest> grayRequestThreadLocal = new ThreadLocal<>();
    private ThreadLocal<GrayTrackInfo> grayTrackInfoThreadLocal = new ThreadLocal<>();


    public ThreadLocalRequestStorage() {
        this(new LocalStorageLifeCycle.NoOpLocalStorageLifeCycle());
    }

    public ThreadLocalRequestStorage(LocalStorageLifeCycle localStorageLifeCycle) {
        super(localStorageLifeCycle);
    }

    @Override
    public void setGrayTrackInfo(GrayTrackInfo grayTrackInfo) {
        grayTrackInfoThreadLocal.set(grayTrackInfo);
    }

    @Override
    public void removeGrayTrackInfo() {
        grayTrackInfoThreadLocal.remove();
    }

    @Override
    public GrayTrackInfo getGrayTrackInfo() {
        return grayTrackInfoThreadLocal.get();
    }

    @Override
    public void setGrayRequest(GrayRequest grayRequest) {
        grayRequestThreadLocal.set(grayRequest);
    }

    @Override
    public void removeGrayRequest() {
        grayRequestThreadLocal.remove();
    }

    @Override
    public GrayRequest getGrayRequest() {
        return grayRequestThreadLocal.get();
    }


}
