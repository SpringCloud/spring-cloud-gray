package cn.springcloud.gray.server.module.domain;

import lombok.Getter;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-03-21 22:24
 */
@Getter
public enum DelFlag {


    ALL(-1, null), DELELTED(1, true), UNDELETE(0, false);

    DelFlag(int flag, Boolean del) {
        this.flag = flag;
        this.del = del;
    }

    private int flag;
    private Boolean del;

    public static DelFlag ofFlag(int flag) {
        for (DelFlag delFlag : values()) {
            if (Objects.equals(delFlag.getFlag(), flag)) {
                return delFlag;
            }
        }
        return ALL;
    }

    public static DelFlag ofDel(Boolean del) {
        if (Objects.isNull(del)) {
            return ALL;
        }
        return del ? DELELTED : UNDELETE;
    }

}
