package cn.springcloud.gray.eureka.domain;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRes {
    private String code;
    private Object data;
}
