package cn.springcloud.gray.server.module.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ServiceOwner {
    private String userId;
    private String serviceId;
    private String operator;
    private Date operateTime;
}
