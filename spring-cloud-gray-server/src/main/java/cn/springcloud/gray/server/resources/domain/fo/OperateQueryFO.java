package cn.springcloud.gray.server.resources.domain.fo;

import cn.springcloud.gray.server.module.audit.domain.OperateQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;


@ApiModel
public class OperateQueryFO {

    @ApiModelProperty("操作人的id")
    private String operator;
    @ApiModelProperty("操作人的ip")
    private String ip;
    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty
    private Date startTime;
    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty
    private Date endTime;
    @ApiModelProperty("结果的code码")
    private String apiResCode;
    @ApiModelProperty("操作的Uri")
    private String uri;
    @ApiModelProperty("操作的RequestHandler")
    private String handler;
    /**
     * 操作结论, -1:all, 0:failed, 1: scuuessed
     */
    @ApiModelProperty(value = "操作结论, -1:all, 0:failed, 1: scuuessed", example = "1",allowableValues = "-1,0,1")
    private int operateState;



    public OperateQuery toOperateQuery(){
        return OperateQuery.builder()
                .apiResCode(apiResCode)
                .operateEndTime(endTime)
                .operateStartTime(startTime)
                .ip(ip)
                .uri(uri)
                .handler(handler)
                .operator(operator)
                .operateState(operateState == -1 ? null : operateState)
                .build();
    }


    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getApiResCode() {
        return apiResCode;
    }

    public void setApiResCode(String apiResCode) {
        this.apiResCode = apiResCode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public int getOperateState() {
        return operateState;
    }

    public void setOperateState(int operateState) {
        this.operateState = operateState;
    }
}
