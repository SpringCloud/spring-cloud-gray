package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-01-30 11:54
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gray_event_log`", indexes = {@Index(columnList = "source_id"), @Index(columnList = "timestamp")})
public class GrayEventLogDO {

    @Id
    @Column(length = 32)
    private String id;
    private String type;
    @Column(name = "source_id", length = 64)
    private String sourceId;
    private Long timestamp;
    @Column(name = "create_time")
    private Date createTime;
    @Column(length = 32)
    private String operator;
    @Column(name = "del_flag")
    private Boolean delFlag;

}
