package cn.springcloud.gray.utils;

import cn.springcloud.gray.decision.factory.GrayDecisionFactory;

public class NameUtils {


    private NameUtils() {
        throw new AssertionError("Must not instantiate utility class.");
    }

    /**
     * Generated name prefix.
     */
    public static final String GENERATED_NAME_PREFIX = "_genkey_";

    public static String generateName(int i) {
        return GENERATED_NAME_PREFIX + i;
    }


    public static String normalizeDecisionFactoryName(
            Class<? extends GrayDecisionFactory> clazz) {
        return removeGarbage(clazz.getSimpleName()
                .replace(GrayDecisionFactory.class.getSimpleName(), ""));
    }

    public static <T> String normalizeName(
            Class<? extends T> clazz, Class<T> cls) {
        return removeGarbage(clazz.getSimpleName()
                .replace(cls.getSimpleName(), ""));
    }

    private static String removeGarbage(String s) {
        int garbageIdx = s.indexOf("$Mockito");
        if (garbageIdx > 0) {
            return s.substring(0, garbageIdx);
        }

        return s;
    }

}
