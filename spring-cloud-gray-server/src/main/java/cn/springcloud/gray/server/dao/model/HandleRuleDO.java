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
@Table(name = "handle_rule", indexes = {@Index(columnList = "namespace"), @Index(columnList = "type")})
public class HandleRuleDO {

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 64)
    private String namespace;
    @Column(length = 64)
    private String type;
    @Column(length = 128)
    private String moduleId;
    @Column(length = 128)
    private String resource;
    @Column(length = 256)
    private Long[] matchingPolicyIds;
    @Column(length = 64)
    private String handleOption;
    @Column(name = "`order`", length = 4)
    private Integer order;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;

}
