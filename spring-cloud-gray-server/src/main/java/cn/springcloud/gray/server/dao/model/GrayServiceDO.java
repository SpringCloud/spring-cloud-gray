package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gray_service")
public class GrayServiceDO {

    @Id
    @Column(length = 32)
    private String serviceId;
    @Column(length = 64)
    private String serviceName;
    @Column(length = 4)
    private Integer instanceNumber;
    @Column(length = 4)
    private Integer grayInstanceNumber;
    @Column(length = 256, name = "des")
    private String describe;

}
