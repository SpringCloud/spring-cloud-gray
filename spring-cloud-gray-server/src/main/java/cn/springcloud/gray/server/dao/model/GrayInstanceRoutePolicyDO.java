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
@Table(name = "gray_instance_route_policy", indexes = {@Index(columnList = "instanceId"), @Index(columnList = "policyId")})
public class GrayInstanceRoutePolicyDO {
    @Id
    @Column(length = 32)
    private String id;
    @Column(length = 64)
    private String instanceId;
    @Column(length = 20)
    private Long policyId;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
