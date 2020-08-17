package cn.springcloud.gray.server.dao.model;

import cn.springcloud.gray.model.HandleType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-05-31 10:03
 */
@Data
@Entity
@Table(name = "handle", indexes = {@Index(columnList = "namespace"), @Index(columnList = "type")})
public class HandleDO {
    @Id
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String namespace;
    /**
     * 处理类型: 比如Mock
     * 详情请看{@link HandleType#code()}
     */
    @Column(length = 64)
    private String type;

    @Column
    private Boolean delFlag;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
