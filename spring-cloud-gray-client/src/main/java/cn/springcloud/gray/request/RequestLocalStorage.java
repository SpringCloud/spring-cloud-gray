package cn.springcloud.gray.request;

public interface RequestLocalStorage {


    void initContext();

    void setGrayTrackInfo(GrayTrackInfo grayTrackInfo);

    void removeGrayTrackInfo();

    GrayTrackInfo getGrayTrackInfo();

    void setGrayRequest(GrayRequest grayRequest);

    void removeGrayRequest();

    GrayRequest getGrayRequest();

    void closeContext();
}
