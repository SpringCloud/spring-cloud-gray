package cn.springcloud.gray.request;

public class ThreadLocalRequestStorage implements RequestLocalStorage {
    private ThreadLocal<GrayRequest> grayRequestThreadLocal = new ThreadLocal<>();
    private ThreadLocal<GrayTrackInfo> grayTrackInfoThreadLocal = new ThreadLocal<>();

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
