package cn.springcloud.gray.performance.agent;

import cn.springcloud.gray.performance.PerformanceLogger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;


/**
 * -javaagent:{dir}/fm-orm-0.0.1-SNAPSHOT.jar={string args}
 */
public class GrayPerformanceAgent {

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        PerformanceLogger.start();
        // 添加Transformer
//        inst.addTransformer(new GrayPerformanceTransformer(agentOps));


        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {

            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {

            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

        };

        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                return builder
                        .method(ElementMatchers.<MethodDescription>any()) // 拦截任意方法
                        .intercept(MethodDelegation.to(TimeInterceptor.class)); // 委托
            }

        };


        new AgentBuilder
                .Default()
                .ignore(ElementMatchers.nameStartsWith("cn.springcloud.gray.service.b")
                        .or(ElementMatchers.nameStartsWith("cn.springcloud.gray.zuul")))
                .type(ElementMatchers.nameStartsWith("cn.springcloud.gray")) // 指定需要拦截的类
                .transform(transformer)
                .with(listener)
                .installOn(inst);

        Runtime.getRuntime().addShutdownHook(PerformanceLogger.getHookThread());
    }


}
