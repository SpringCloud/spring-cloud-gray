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
@Table(name = "user_resource_authority", indexes = {@Index(columnList = "resource,resourceId"), @Index(columnList = "userId")})
public class UserResourceAuthorityDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 32)
    private String userId;
    @Column(length = 128)
    private String resource;
    @Column(length = 128)
    private String resourceId;
    @Column(length = 4)
    private Integer authorityFlag;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;


}
