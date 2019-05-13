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
@Table(name = "gray_instance")
public class GrayPolicyDO {

    @Id
    @Column(length = 20)
    private Long id;
    @Column(length = 64)
    private String instanceId;
    @Column(length = 256)
    private String alias;
}
