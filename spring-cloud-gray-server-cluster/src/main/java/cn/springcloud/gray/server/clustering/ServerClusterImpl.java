package cn.springcloud.gray.server.clustering;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author saleson
 * @date 2020-08-16 00:08
 */
public class ServerClusterImpl implements ServerCluster {

    private static final String[] LOCAL_HOST_ALIAS = {"localhost", "127.0.0.1"};

    private volatile Set<PeerNode> peerNodes = new HashSet<>();
    private PeerNode localServer;

    public ServerClusterImpl(PeerNode localServer) {
        this.localServer = localServer;
    }

    @Override
    public String[] getPeerNodeUrls() {
        List<String> urls = peerNodes.stream()
                .map(PeerNode::getHttpUrl)
                .collect(Collectors.toList());
        return urls.toArray(new String[urls.size()]);
    }

    @Override
    public PeerNode[] getPeerNodes() {
        return peerNodes.toArray(new PeerNode[peerNodes.size()]);
    }

    @Override
    public String[] exceptItselfPeerNodesUrls() {
        PeerNode[] peerNodes = exceptItselfPeerNodes();
        String[] urls = new String[peerNodes.length];
        for (int i = 0; i < peerNodes.length; i++) {
            urls[i] = peerNodes[i].getHttpUrl();
        }
        return urls;
    }

    @Override
    public PeerNode[] exceptItselfPeerNodes() {
        List<PeerNode> nodes = peerNodes.stream()
                .filter(n -> !isLocalNode(n))
                .collect(Collectors.toList());
        return nodes.toArray(new PeerNode[nodes.size()]);
    }

    @Override
    public void registerPeerNode(PeerNode peerNode) {

        if (peerNodes.contains(peerNode)) {
            return;
        }
        synchronized (this) {
            Set<PeerNode> peerNodes = new HashSet<>(this.peerNodes);
            peerNodes.add(peerNode);
            this.peerNodes = peerNodes;
        }
    }

    @Override
    public void removePeerNode(PeerNode peerNode) {
        if (!peerNodes.contains(peerNode)) {
            return;
        }
        synchronized (this) {
            Set<PeerNode> peerNodes = new HashSet<>(this.peerNodes);
            peerNodes.remove(peerNode);
            this.peerNodes = peerNodes;
        }
    }

    protected boolean isLocalNode(PeerNode peerNode) {
        if (!ArrayUtils.contains(LOCAL_HOST_ALIAS, peerNode.getHost())
                && !StringUtils.equals(peerNode.getHost(), localServer.getHost())) {
            return false;
        }
        if (!Objects.equals(peerNode.getPort(), localServer.getPort())) {
            return false;
        }
        return true;
    }
}
