package cn.springcloud.gray.routing.connectionpoint;


import java.io.IOException;

public interface RoutingConnectionPoint {


    default <T> T execute(RoutingConnectPointContext connectPointContext, Supplier<T> supplier) throws IOException {
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


    void executeConnectPoint(RoutingConnectPointContext connectPointContext);


    void shutdownconnectPoint(RoutingConnectPointContext connectPointContext);


    public interface Supplier<T> {
        T get() throws IOException;
    }

}
