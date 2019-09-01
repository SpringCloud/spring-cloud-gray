package cn.springcloud.gray.server.module.audit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperateQuery {
    private String operator;
    private String ip;
    private Date operateStartTime;
    private Date operateEndTime;
    private String apiResCode;
    private String uri;
    private String handler;
    /**
     * 操作结论, -1:all, 0:failed, 1: scuuessed
     */
    private Integer operateState;
}
