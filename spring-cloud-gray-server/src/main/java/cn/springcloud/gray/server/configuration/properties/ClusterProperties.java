package cn.springcloud.gray.server.configuration.properties;

import cn.springcloud.gray.server.clustering.PeerNode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author saleson
 * @date 2020-08-16 00:29
 */
@Data
@ConfigurationProperties(prefix = "gray.server.cluster")
public class ClusterProperties {
    private List<PeerNode> peerNodes = new ArrayList<>();
}
