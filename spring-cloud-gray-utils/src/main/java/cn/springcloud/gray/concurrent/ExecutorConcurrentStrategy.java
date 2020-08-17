package cn.springcloud.gray.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author saleson
 * @date 2020-08-16 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecutorConcurrentStrategy {
    private int corePoolSize = 10;
    private int maximumPoolSize = 10;
    private int queueSize = 10;
}
