package cn.springcloud.gray.choose;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-05-27 23:25
 */
public enum ChooseGroup {
    ALL, GRAY, NORMAL;


    public static ChooseGroup ofNameDefaultAll(String name) {
        for (ChooseGroup value : values()) {
            if (Objects.equals(name, value.name())) {
                return value;
            }
        }
        return ALL;
    }
}
