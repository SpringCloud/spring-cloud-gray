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
@Table(name = "gray_service", indexes = {@Index(columnList = "namespace")})
public class GrayServiceDO {

    @Id
    @Column(length = 128)
    private String serviceId;
    @Column(length = 128)
    private String serviceName;
    @Column(length = 64)
    private String contextPath;
    @Column(length = 64)
    private String namespace;
    @Column(length = 4)
    private Integer instanceNumber;
    @Column(length = 4)
    private Integer grayInstanceNumber;
    @Column(length = 128, name = "des")
    private String describe;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;

}
