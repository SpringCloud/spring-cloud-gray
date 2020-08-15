package cn.springcloud.gray.server.clustering.synchro;

/**
 * @author saleson
 * @date 2020-08-14 00:14
 */
public interface ServerSynchronizer {

    void broadcast(SynchData synchData);


}
