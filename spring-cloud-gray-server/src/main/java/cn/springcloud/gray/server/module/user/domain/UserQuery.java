package cn.springcloud.gray.server.module.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuery {

    public static final int STATUS_ALL = -1;

    private String key;
    /**
     * -1: 全部， 0: 禁用，1:启用
     */
    private int status;
}
