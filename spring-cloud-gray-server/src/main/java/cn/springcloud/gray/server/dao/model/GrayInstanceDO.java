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
@Table(name = "gray_instance", indexes = {@Index(columnList = "serviceId")})
public class GrayInstanceDO {
    @Id
    @Column(length = 64)
    private String instanceId;
    @Column(length = 128)
    private String serviceId;
    @Column(length = 32)
    private String host;
    @Column(length = 5)
    private Integer port;
    @Column(length = 128)
    private String des;
    @Column
    private Date lastUpdateDate;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
    @Column
    private String[] aliases;

    /**
     * 实例状态
     */
    @Column(length = 16)
    private String instanceStatus;
    /**
     * 灰度状态
     */
    @Column(length = 16)
    private String grayStatus;

    /**
     * 灰度锁定
     */
    @Column(length = 2)
    private Integer grayLock;

}
