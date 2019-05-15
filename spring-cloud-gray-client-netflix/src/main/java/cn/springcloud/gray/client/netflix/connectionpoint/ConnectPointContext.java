package cn.springcloud.gray.client.netflix.connectionpoint;

import cn.springcloud.gray.request.GrayRequest;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectPointContext {

    private static final ThreadLocal<ConnectPointContext> contextLocal = new ThreadLocal<>();


    private GrayRequest grayRequest;
    @Setter
    private Throwable throwable;

    private String interceptroType;


    static void setContextLocal(ConnectPointContext cxt) {
        contextLocal.set(cxt);
    }

    static void removeContextLocal() {
        contextLocal.remove();
    }


    public static ConnectPointContext getContextLocal() {
        return contextLocal.get();
    }
}
