package cn.springcloud.gray.server.configuration;

import cn.springcloud.gray.server.clustering.PeerNode;
import cn.springcloud.gray.server.clustering.ServerCluster;
import cn.springcloud.gray.server.clustering.ServerClusterImpl;
import cn.springcloud.gray.server.configuration.properties.ClusterProperties;
import cn.springcloud.gray.utils.NetworkUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

/**
 * @author saleson
 * @date 2020-08-16 00:13
 */
@Configuration
@EnableConfigurationProperties({ClusterProperties.class})
public class ServerClusterAutoConfiguration {

    @Autowired
    private ClusterProperties clusterProperties;
    @Value("${server.port}")
    private int serverPort;

    @Bean
    @ConditionalOnMissingBean
    public ServerCluster serverCluster() throws UnknownHostException {
        PeerNode peerNode = new PeerNode();
        String localId = NetworkUtils.getLocalIp();
        if (StringUtils.isEmpty(localId)) {
            throw new NullPointerException("无法获取到本机ip");
        }
        peerNode.setHost(localId);
        peerNode.setPort(serverPort);
        ServerCluster serverCluster = new ServerClusterImpl(peerNode);
        for (PeerNode node : clusterProperties.getPeerNodes()) {
            serverCluster.registerPeerNode(node);
        }
        return serverCluster;
    }

}
