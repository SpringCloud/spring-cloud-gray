package cn.springcloud.gray;


import cn.springcloud.gray.request.GrayRequest;
import org.springframework.core.Ordered;

/**
 *
 */
public interface RequestInterceptor extends Ordered {

    String interceptroType();

    default boolean shouldIntercept() {
        return true;
    }

    boolean pre(GrayRequest request);

    boolean after(GrayRequest request);

    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
