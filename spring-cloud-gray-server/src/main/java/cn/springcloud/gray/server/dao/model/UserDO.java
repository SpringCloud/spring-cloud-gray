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
@Table(name = "user", indexes = {@Index(columnList = "account"), @Index(columnList = "name")})
public class UserDO {

    @Id
    @Column(length = 32)
    private String userId;
    @Column(length = 128)
    private String password;
    @Column(length = 126)
    private String name;
    @Column(length = 32)
    private String account;
    @Column(length = 64)
    private String roles;
    @Column(length = 4)
    private Integer status;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
    @Column
    private Date createTime;


}
