package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author saleson
 * @date 2020-08-11 19:17
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "default_namespace")
public class DefaultNamespaceDO {
    @Id
    @Column(length = 32)
    private String userId;

    @Column(length = 32)
    private String nsCode;


}
