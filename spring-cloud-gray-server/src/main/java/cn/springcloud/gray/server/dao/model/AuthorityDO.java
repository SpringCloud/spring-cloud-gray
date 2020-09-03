package cn.springcloud.gray.server.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-21 21:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority", indexes = {@Index(columnList = "role,resource")})
public class AuthorityDO {

    @Id
    @Column
    private Long id;
    @Column(length = 32)
    private String role;
    @Column(length = 128)
    private String resource;
    @Column(length = 64)
    private String[] authorities;
    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
