package cn.springcloud.gray.utils;

import org.apache.commons.collections.MapUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-02-04 23:49
 */
public class BeanUtils {

    /**
     * 将对象的字段值转换成string map
     *
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object value = field.get(obj);
            map.put(field.getName(), value);
        }

        return map;
    }

    /**
     * 将对象的字段值转换成string map
     *
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toStringMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
        if (obj == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, String> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object value = field.get(obj);
            map.put(field.getName(), value == null ? null : value.toString());
        }

        return map;
    }
}
