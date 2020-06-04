package cn.springcloud.gray.server.dao.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-05-31 10:03
 */
@Data
@Entity
@Table(name = "handle_action", indexes = {@Index(columnList = "handleId")})
public class HandleActionDO {
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20)
    private Long handleId;
    @Column(length = 64)
    private String actionName;
    @Column(length = 256)
    private String infos;
    @Column(name = "`order`", length = 4)
    private Integer order;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
