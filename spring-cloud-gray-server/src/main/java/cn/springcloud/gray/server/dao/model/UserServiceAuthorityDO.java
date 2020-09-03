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
@Table(name = "user_service_authority", indexes = {@Index(columnList = "serviceId"), @Index(columnList = "userId")})
public class UserServiceAuthorityDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 32)
    private String userId;
    @Column(length = 128)
    private String serviceId;
    @Column(length = 32)
    private String operator;
    @Column
    private Date operateTime;


}
