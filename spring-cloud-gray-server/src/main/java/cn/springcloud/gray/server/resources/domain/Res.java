package cn.springcloud.gray.server.resources.domain;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Res<T> {

    private String code;
    private String msg;
    private T result;
}
