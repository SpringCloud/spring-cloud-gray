package cn.springcloud.gray.server.module.gray.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author saleson
 * @date 2020-01-31 22:07
 */
@Data
public class GrayEventLog {

    private Long id;
    private String eventClass;
    private String type;
    private String sourceId;
    private Long sortMark;
    private String content;
    private Date createTime;
    private Boolean delFlag;
}
