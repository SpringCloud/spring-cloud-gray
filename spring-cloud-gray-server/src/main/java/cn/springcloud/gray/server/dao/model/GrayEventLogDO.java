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
@Table(name = "gray_event_log", indexes = {@Index(columnList = "sourceId"), @Index(columnList = "createTime")})
public class GrayEventLogDO {

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    @Column(length = 128)
    private String sourceId;
    @Column(length = 20)
    private Long sortMark;
    @Column(length = 2048)
    private String content;
    @Column
    private Date createTime;
    @Column
    private Boolean delFlag;

}
