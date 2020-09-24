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
@Table(name = "gray_track", indexes = {@Index(columnList = "serviceId,name", unique = true), @Index(columnList = "instanceId")})
public class GrayTrackDO {

    @Id
//    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 128)
    private String serviceId;
    @Column(length = 64)
    private String instanceId;
    @Column(length = 64)
    private String name;
    @Column(length = 256)
    private String infos;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
