package cn.springcloud.gray.client.netflix.connectionpoint;


import java.io.IOException;

public interface RibbonConnectionPoint {


    default <T> T execute(ConnectPointContext connectPointContext, Supplier<T> supplier) throws IOException {
        try {
            executeConnectPoint(connectPointContext);
            return supplier.get();
        } catch (Exception e) {
            connectPointContext.setThrowable(e);
            throw e;
        } finally {
            shutdownconnectPoint(connectPointContext);
        }
    }


    void executeConnectPoint(ConnectPointContext connectPointContext);


    void shutdownconnectPoint(ConnectPointContext connectPointContext);


    public interface Supplier<T> {
        T get() throws IOException;
    }

}
