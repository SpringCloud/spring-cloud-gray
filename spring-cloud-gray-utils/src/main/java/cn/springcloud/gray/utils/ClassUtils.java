package cn.springcloud.gray.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassUtils {


    public static final ParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public static String[] getParameterNames(Method method) {
        return getParameterNames(localVariableTableParameterNameDiscoverer, method);
    }

    public static String[] getParameterNames(Constructor<?> ctor) {
        return getParameterNames(localVariableTableParameterNameDiscoverer, ctor);
    }


    public static String[] getParameterNames(ParameterNameDiscoverer parameterNameDiscoverer, Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }

    public static String[] getParameterNames(ParameterNameDiscoverer parameterNameDiscoverer, Constructor<?> ctor) {
        return parameterNameDiscoverer.getParameterNames(ctor);
    }


}
