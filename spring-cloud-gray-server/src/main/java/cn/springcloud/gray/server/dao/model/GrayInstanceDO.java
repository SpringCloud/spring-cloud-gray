package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String instanceId;
    @Column(length = 32)
    private String serviceId;
    @Column(length = 32)
    private String host;
    @Column(length = 5)
    private Integer port;

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


}
