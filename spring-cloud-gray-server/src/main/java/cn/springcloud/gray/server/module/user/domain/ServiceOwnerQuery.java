package cn.springcloud.gray.server.module.user.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceOwnerQuery {

    public static final int QUERY_ITEM_ALL = 0;
    public static final int QUERY_ITEM_BINDED = 1;
    public static final int QUERY_ITEM_UNBINDED = 2;

    private String serviceId;
    /**
     * 查询项,{0:全部, 1:已绑定owner, 2:未绑定owner}
     */
    private int queryItem;
}
