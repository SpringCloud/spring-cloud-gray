package cn.springcloud.gray.server.dao.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author saleson
 * @date 2020-03-01 22:03
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "namespace")
public class NamespaceDO {
    @Id
    @Column(length = 32)
    private String code;

    @Column(length = 64)
    private String name;

    @Column
    private Boolean delFlag;

    @Column
    private Date createTime;

    @Column(length = 32)
    private String creator;

}
