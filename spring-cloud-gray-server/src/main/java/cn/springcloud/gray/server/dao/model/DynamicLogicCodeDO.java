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
@Table(name = "dynamic_logic_code", indexes = {@Index(columnList = "logicCode")})
public class DynamicLogicCodeDO {
    @Id
    @Column(length = 20)
    private Long id;
    @Column(length = 64)
    private String logicCode;
    @Column(length = 128)
    private String code;
    @Column
    private Boolean enabled;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
