package cn.springcloud.gray.server.module.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saleson
 * @date 2020-03-22 00:16
 */
@Getter
public enum ResourceAuthorityFlag {

    OWNER(9), ADMIN(1);


    private static Map<Integer, ResourceAuthorityFlag> flagMappings = new HashMap<>();

    private int flag;

    ResourceAuthorityFlag(int flag) {
        this.flag = flag;
    }

    static {
        for (ResourceAuthorityFlag oa : values()) {
            flagMappings.put(oa.flag, oa);
        }
    }


    public static ResourceAuthorityFlag ofFlag(int flag) {
        return flagMappings.get(flag);
    }
}
