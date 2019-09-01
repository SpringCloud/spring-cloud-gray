package cn.springcloud.gray.server.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_owner", indexes = {@Index(columnList = "userId")})
public class ServiceOwnerDO {
    @Id
    private String serviceId;
    @Column(length = 32)
    private String userId;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;
}
