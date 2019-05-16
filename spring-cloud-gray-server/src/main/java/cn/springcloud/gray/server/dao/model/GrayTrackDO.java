package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gray_track", indexes = {@Index(columnList = "serviceId"), @Index(columnList = "instanceId")})
public class GrayTrackDO {

    @Id
//    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 32)
    private String serviceId;
    @Column(length = 64)
    private String instanceId;
    @Column(length = 64)
    private String name;
    @Column(length = 256)
    private String infos;
}
