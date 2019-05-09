package cn.springcloud.gray;


import cn.springcloud.gray.request.GrayRequest;
import org.springframework.core.annotation.Order;

/**
 *
 */
public interface RequestInterceptor extends Order {

    String interceptroType();

    boolean shouldIntercept();

    boolean pre(GrayRequest request);

    boolean after(GrayRequest exchange);
}
