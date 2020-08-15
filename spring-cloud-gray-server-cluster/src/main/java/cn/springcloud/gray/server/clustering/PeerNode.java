package cn.springcloud.gray.server.clustering;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-08-16 00:04
 */
@Data
public class PeerNode {
    private String host;
    private int port;

    public String getHttpUrl() {
        return "http://" + host + ":" + port;
    }
}
