package cn.springcloud.gray.server.resources.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRes<T> {

    public static final String CODE_SUCCESS = "0";
    public static final String CODE_NOT_FOUND = "404";



    private String code = "0";
    private String message;
    private T data;
}
