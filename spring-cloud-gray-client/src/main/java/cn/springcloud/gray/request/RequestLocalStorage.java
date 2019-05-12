package cn.springcloud.gray.request;

public interface RequestLocalStorage {


    void setGrayTrackInfo(GrayTrackInfo grayTrackInfo);

    void removeGrayTrackInfo();

    GrayTrackInfo getGrayTrackInfo();

    void setGrayRequest(GrayRequest grayRequest);

    void removeGrayRequest();

    GrayRequest getGrayRequest();
}
