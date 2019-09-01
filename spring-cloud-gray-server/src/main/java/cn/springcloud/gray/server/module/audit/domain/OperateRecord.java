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
public class OperateRecord {

    public static final int OPERATE_STATE_SCUUESSED = 1;
    public static final int OPERATE_STATE_FAILED = 0;

    private String ip = "";
    private String operator = "";
    private Date operateTime;
    private String uri = "";
    private String httpMethod = "";
    private String queryString = "";
    private String handler = "";
    private String headlerArgs = "";
    private String apiResCode = "";
    /**
     * 操作结论, 0:failed, 1: scuuessed
     */
    private int operateState;
}
