package cn.springcloud.gray.performance.agent;

import cn.springcloud.gray.performance.PerformanceLogger;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TimeInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.nanoTime();
        try {
            // 原有函数执行
            return callable.call();
        } finally {
            PerformanceLogger.printMethodUsedTime(method.getDeclaringClass().getName() + "#" + method.getName(), (System.nanoTime() - start));
        }
    }

}
