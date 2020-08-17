package cn.springcloud.gray.server.module.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserInfo {

    public static final int STATUS_ENABLED = 1;
    public static final int STATUS_DISABLED = 0;

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_EDITOR = "editor";

    private String userId;
    private String name;
    private String account;
    private String[] roles;
    private int status = STATUS_ENABLED;
    private String operator;
    private Date operateTime;


    public boolean isAdmin() {
        return ArrayUtils.contains(this.roles, ROLE_ADMIN);
    }

    public boolean isEnabled() {
        return Objects.equals(status, STATUS_ENABLED);
    }

    public static boolean isEnable(UserInfo userInfo) {
        return !Objects.isNull(userInfo) && userInfo.isEnabled();
    }
}
