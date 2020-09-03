package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-01 22:09
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "route_policy_record", indexes = {@Index(columnList = "moduleId,resource"), @Index(columnList = "policyId"), @Index(columnList = "namespace")})
public class RoutePolicyRecordDO {
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 64)
    private String namespace;
    @Column(length = 32)
    private String type;
    @Column(length = 128)
    private String moduleId;
    @Column(length = 128)
    private String resource;
    @Column(length = 20)
    private Long policyId;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
