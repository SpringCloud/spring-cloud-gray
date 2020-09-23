package cn.springcloud.gray.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-09-23 23:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcurrnetProperties {
    private int corePoolSize = 5;
    private int maximumPoolSize = 20;
    private long keepAliveTime = 30000;
    private int queueSize = 10;
    private String threadPrefix;
}
