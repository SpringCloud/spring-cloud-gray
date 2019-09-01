package cn.springcloud.gray.routing.connectionpoint;

import cn.springcloud.gray.request.GrayRequest;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoutingConnectPointContext {

    private static final ThreadLocal<RoutingConnectPointContext> contextLocal = new ThreadLocal<>();


    private GrayRequest grayRequest;
    @Setter
    private Throwable throwable;

    private String interceptroType;


    static void setContextLocal(RoutingConnectPointContext cxt) {
        contextLocal.set(cxt);
    }

    static void removeContextLocal() {
        contextLocal.remove();
    }


    public static RoutingConnectPointContext getContextLocal() {
        return contextLocal.get();
    }
}
