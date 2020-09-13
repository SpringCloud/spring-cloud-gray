package cn.springcloud.gray.client.dubbo;

import cn.springcloud.gray.RequestInterceptor;
import cn.springcloud.gray.client.dubbo.constants.GrayDubboConstants;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.apache.dubbo.rpc.Invocation;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-09-12 12:28
 */
public class DubboRequestInterceptor implements RequestInterceptor {
    @Override
    public String interceptroType() {
        return GrayDubboConstants.INTERCEPTRO_TYPE_DUBBO;
    }

    @Override
    public boolean pre(GrayRequest request) {
        GrayTrackInfo grayTrack = request.getGrayTrackInfo();
        if (Objects.nonNull(grayTrack)) {
            recordGrayTrack(request);
        }
        return true;
    }


    private void recordGrayTrack(GrayRequest request) {
        Invocation invocation = (Invocation) request.getAttachment(GrayDubboConstants.INVOKE_INVOCATION);
        if (Objects.isNull(invocation)) {
            return;
        }
        GrayTrackInfo grayTrackInfo = request.getGrayTrackInfo();
        if (Objects.isNull(grayTrackInfo)) {
            return;
        }

        invocation.setAttachment(GrayTrackInfo.GRAY_TRACK_PREFIX, grayTrackInfo.toMap());
    }

    @Override
    public boolean after(GrayRequest request) {
        return true;
    }


}
