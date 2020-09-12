package cn.springcloud.gray.client.dubbo.cluster;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;

/**
 * @author saleson
 * @date 2020-09-11 19:41
 */
public class GrayClusterWrapper implements Cluster {

    private Cluster cluster;

    public GrayClusterWrapper(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new GrayClusterInvoker<>(directory,
                this.cluster.join(directory));
    }
}
