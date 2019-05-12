package cn.springcloud.gray.eureka.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ApiRes {
    private String code;
    private Object data;
}
