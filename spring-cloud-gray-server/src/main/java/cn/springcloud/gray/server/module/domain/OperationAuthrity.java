package cn.springcloud.gray.server.module.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-03-21 23:19
 */
@Getter
public enum OperationAuthrity {
    READ("read", "读取"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除");

    private static Map<String, OperationAuthrity> codeMappings = new HashMap<>();

    private String code;
    private String name;

    OperationAuthrity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    static {
        for (OperationAuthrity oa : values()) {
            codeMappings.put(oa.code, oa);
        }
    }

    public static OperationAuthrity ofCode(String code) {
//        for (OperationAuthrity oa : values()) {
//            if (Objects.equals(oa.code, code)) {
//                return oa;
//            }
//        }
//        return null;
        return codeMappings.get(code);
    }
}
