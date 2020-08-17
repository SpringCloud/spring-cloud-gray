package cn.springcloud.gray.server.clustering.synchro;

/**
 * @author saleson
 * @date 2020-08-16 05:03
 */
public interface SynchDataListener {

    String supportListenDatatype();

    void listen(SynchData synchData);
}
