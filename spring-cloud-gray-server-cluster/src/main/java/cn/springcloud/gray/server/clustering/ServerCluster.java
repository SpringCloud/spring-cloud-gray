package cn.springcloud.gray.server.clustering;

/**
 * @author saleson
 * @date 2020-08-15 23:59
 */
public interface ServerCluster {

    String[] getPeerNodeUrls();

    PeerNode[] getPeerNodes();

    String[] exceptItselfPeerNodesUrls();

    PeerNode[] exceptItselfPeerNodes();

    void registerPeerNode(PeerNode peerNode);

    void removePeerNode(PeerNode peerNode);
}
