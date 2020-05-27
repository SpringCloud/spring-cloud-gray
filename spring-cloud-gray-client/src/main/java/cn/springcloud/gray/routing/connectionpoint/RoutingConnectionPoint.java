package cn.springcloud.gray.routing.connectionpoint;


import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

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

    /**
     * @param connectPointContext
     * @param supplier              结果
     * @param mockResultConvertFunc
     * @param <T>
     * @return
     */
    default <T> T executeOrMock(RoutingConnectPointContext connectPointContext, Supplier<T> supplier, Function<Object, T> mockResultConvertFunc) {
        try {
            executeConnectPoint(connectPointContext);
            T result = excuteMockConvert(connectPointContext, mockResultConvertFunc);
            if (Objects.nonNull(result)) {
                return result;
            }

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

    default <T> T excuteMockConvert(RoutingConnectPointContext connectPointContext, Function<Object, T> mockResultConvertFunc) {
        if (Objects.isNull(mockResultConvertFunc)) {
            return null;
        }
        Object mockResult = excuteMockHandle(connectPointContext);
        if (Objects.isNull(mockResult)) {
            return null;
        }
        return mockResultConvertFunc.apply(mockResult);
    }


    /**
     * 执行mock处理
     *
     * @param connectPointContext
     * @return
     */
    Object excuteMockHandle(RoutingConnectPointContext connectPointContext);

    void executeConnectPoint(RoutingConnectPointContext connectPointContext);


    void shutdownconnectPoint(RoutingConnectPointContext connectPointContext);


    public interface Supplier<T> {
        T get() throws IOException;
    }

}
