package cn.springcloud.gray.server.event.longpolling;

import cn.springcloud.gray.event.longpolling.ListenResult;
import lombok.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author saleson
 * @date 2020-02-03 21:05
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class ClientLongPolling {
    private long timeout;
    private long sortMark;
    private String instanceId;
    private String serviceId;
    private DeferredResult<ListenResult> deferredResult;
}
