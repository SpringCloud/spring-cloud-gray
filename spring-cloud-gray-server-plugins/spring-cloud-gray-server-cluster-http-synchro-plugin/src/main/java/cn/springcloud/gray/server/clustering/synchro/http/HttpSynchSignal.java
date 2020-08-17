package cn.springcloud.gray.server.clustering.synchro.http;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-08-15 23:12
 */
@Data
public class HttpSynchSignal {
    private String id;
    private byte[] objectBytes;
}
