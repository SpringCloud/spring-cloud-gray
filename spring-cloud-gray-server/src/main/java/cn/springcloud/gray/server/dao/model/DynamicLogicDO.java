package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-01-31 21:43
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dynamic_logic", indexes = {@Index(columnList = "namespace")})
public class DynamicLogicDO {
    @Id
    @Column(length = 64)
    private String code;
    @Column(length = 64)
    private String name;
    @Column(length = 256, name = "des")
    private String describe;
    @Column(length = 32)
    private String namespace;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
