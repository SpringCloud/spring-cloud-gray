package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gray_policy", indexes = {@Index(columnList = "instanceId")})
public class GrayPolicyDO {

    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 64)
    private String instanceId;
    @Column(length = 256, name = "alias_name")
    private String alias;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
