package cn.springcloud.gray.client.dubbo.rpc.filter;

import cn.springcloud.gray.GrayClientHolder;
import cn.springcloud.gray.function.Consumer2;
import cn.springcloud.gray.request.GrayTrackInfo;
import cn.springcloud.gray.request.RequestLocalStorage;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

/**
 * @author saleson
 * @date 2020-09-12 11:36
 */
@Activate(group = PROVIDER, order = -10)
public class DubboGrayTrackReceiveFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RequestLocalStorage requestLocalStorage = GrayClientHolder.getRequestLocalStorage();
        if (Objects.isNull(requestLocalStorage)) {
            return invoker.invoke(invocation);
        }
        GrayTrackInfo grayTrackInfo = new GrayTrackInfo();
        receiveGrayTrackInfo(invocation, grayTrackInfo);
        requestLocalStorage.getLocalStorageLifeCycle().initContext(DubboGrayTrackReceiveFilter.class.getName());
        try {
            requestLocalStorage.setGrayTrackInfo(grayTrackInfo);
            return invoker.invoke(invocation);
        } finally {
            requestLocalStorage.removeGrayTrackInfo();
            requestLocalStorage.getLocalStorageLifeCycle().closeContext(DubboGrayTrackReceiveFilter.class.getName());
        }
    }

    private void receiveGrayTrackInfo(Invocation invocation, GrayTrackInfo grayTrackInfo) {
        Map<String, Object> trackMap = (Map<String, Object>) invocation.getObjectAttachment(GrayTrackInfo.GRAY_TRACK_PREFIX);
        if (Objects.isNull(trackMap)) {
            return;
        }

        Map<String, String> attrs = (Map<String, String>) trackMap.get(GrayTrackInfo.ATTRIBUTE_ATTR);
        fillInfos(attrs, grayTrackInfo::setAttribute);

        Map<String, List<String>> headers = (Map<String, List<String>>) trackMap.get(GrayTrackInfo.ATTRIBUTE_HTTP_HEADER);
        fillInfos(headers, grayTrackInfo::setHeader);

        Map<String, List<String>> params = (Map<String, List<String>>) trackMap.get(GrayTrackInfo.ATTRIBUTE_HTTP_PARAMETER);
        fillInfos(params, grayTrackInfo::setParameters);

    }

    private <V> void fillInfos(Map<String, V> map, Consumer2<String, V> consumer) {
        if (Objects.isNull(map)) {
            return;
        }
        map.forEach((k, v) -> consumer.accept(k, v));
    }


}
