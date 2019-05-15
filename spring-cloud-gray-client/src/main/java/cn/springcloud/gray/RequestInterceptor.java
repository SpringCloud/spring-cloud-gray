package cn.springcloud.gray;


import cn.springcloud.gray.request.GrayRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 *
 */
public interface RequestInterceptor extends Ordered {

    String interceptroType();

    boolean shouldIntercept();

    boolean pre(GrayRequest request);

    boolean after(GrayRequest request);

    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
