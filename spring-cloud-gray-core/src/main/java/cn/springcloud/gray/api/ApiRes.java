package cn.springcloud.gray.api;

import lombok.*;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiRes<T> {

    public static final String CODE_SUCCESS = "0";
    public static final String CODE_NOT_FOUND = "404";


    private String code = "0";
    private String message;
    private T data;


    public boolean judgeSuccess() {
        return Objects.equals(CODE_SUCCESS, this.code);
    }

}
