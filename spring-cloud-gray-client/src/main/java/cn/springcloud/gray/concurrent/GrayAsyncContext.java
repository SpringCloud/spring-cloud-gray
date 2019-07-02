package cn.springcloud.gray.concurrent;

import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import lombok.Data;

@Data
public abstract class GrayAsyncContext {

    protected RequestLocalStorage requestLocalStorage;

    protected GrayTrackInfo grayTrackInfo;

}
