package cn.springcloud.gray.routing.connectionpoint;


import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public interface RoutingConnectionPoint {


    default <T> T execute(RoutingConnectPointContext connectPointContext, Supplier<T> supplier) {
        try {
            executeConnectPoint(connectPointContext);
            return supplier.get();
        } catch (Exception e) {
            connectPointContext.setThrowable(e);
            throw new RuntimeException(e);
        } finally {
            shutdownconnectPoint(connectPointContext);
        }
    }

    default <T> T execute(
            RoutingConnectPointContext connectPointContext,
            Supplier<T> supplier,
            Consumer<RoutingConnectPointContext> finalConsumer) {
        try {
            return execute(connectPointContext, supplier);
        } finally {
            if (!Objects.isNull(finalConsumer)) {
                finalConsumer.accept(connectPointContext);
            }
        }
    }


    void executeConnectPoint(RoutingConnectPointContext connectPointContext);


    void shutdownconnectPoint(RoutingConnectPointContext connectPointContext);


    public interface Supplier<T> {
        T get() throws IOException;
    }

}
