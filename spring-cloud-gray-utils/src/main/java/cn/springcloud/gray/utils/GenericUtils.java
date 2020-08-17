package cn.springcloud.gray.utils;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-31 15:05
 */
public class GenericUtils {


    /**
     * @param beanCls
     * @param genericDefineSuperCls 定义泛型的类型，要求是beanCls的父类或beanCls实现的接口
     * @param genericMatchCls       泛型匹配的class
     * @return
     */
    public static boolean match(Class<?> beanCls, Class<?> genericDefineSuperCls, Class<?> genericMatchCls) {
        return match(beanCls, genericDefineSuperCls, genericMatchCls, 0);
    }

    /**
     * @param beanCls
     * @param genericDefineSuperCls 定义泛型的类型，要求是beanCls的父类或beanCls实现的接口
     * @param genericMatchCls       泛型匹配的class
     * @param genericDefineIndex    genericDefineSuperCls 定义的第几个泛型进行匹配
     * @return
     */
    public static boolean match(Class<?> beanCls, Class<?> genericDefineSuperCls, Class<?> genericMatchCls, int genericDefineIndex) {
        Class<?> genericCls = getGenericClass(beanCls, genericDefineSuperCls, genericDefineIndex);

        if (Objects.isNull(genericCls)) {
            return false;
        }
        return ClassUtils.isAssignable(genericMatchCls, genericCls);
    }


    /**
     * @param bean
     * @param genericDefineSuperCls 定义泛型的类型，要求是beanCls的父类或beanCls实现的接口
     * @param genericMatchCls       泛型匹配的class
     * @return
     */
    public static boolean match(Object bean, Class<?> genericDefineSuperCls, Class<?> genericMatchCls) {
        return match(bean, genericDefineSuperCls, genericMatchCls, 0);
    }

    /**
     * @param bean
     * @param genericDefineSuperCls 定义泛型的类型，要求是beanCls的父类或beanCls实现的接口
     * @param genericMatchCls       泛型匹配的class
     * @param genericDefineIndex    genericDefineSuperCls 定义的第几个泛型进行匹配
     * @return
     */
    public static boolean match(Object bean, Class<?> genericDefineSuperCls, Class<?> genericMatchCls, int genericDefineIndex) {
        Class<?> genericCls = getGenericClass(bean.getClass(), genericDefineSuperCls, genericDefineIndex);

        if (AopUtils.isAopProxy(bean)) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            if (targetClass != targetClass.getClass()) {
                genericCls = getGenericClass(targetClass, genericDefineSuperCls, genericDefineIndex);
            }
        }

        if (Objects.isNull(genericCls)) {
            return false;
        }
        return ClassUtils.isAssignable(genericMatchCls, genericCls);
    }


    public static Class<?> getGenericClass(Class<?> beanCls, Class<?> genericDefineSuperCls, int genericDefineIndex) {
        ResolvableType beanType = ResolvableType.forClass(beanCls);
        if (!Objects.isNull(genericDefineSuperCls)) {
            beanType = beanType.as(genericDefineSuperCls);
        }
        ResolvableType genericType = beanType.getGeneric(genericDefineIndex);
        if (Objects.isNull(genericType)) {
            return null;
        }
        Class<?> genericCls = genericType.getRawClass();
        return Objects.nonNull(genericCls) ? genericCls : genericType.resolve();
    }
}
