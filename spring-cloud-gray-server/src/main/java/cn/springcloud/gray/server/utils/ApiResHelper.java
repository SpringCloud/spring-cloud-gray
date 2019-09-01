package cn.springcloud.gray.server.utils;

import cn.springcloud.gray.server.resources.domain.ApiRes;
import org.springframework.http.HttpStatus;

public class ApiResHelper {


    public static <T> ApiRes<T> notAuthority() {
        return ApiRes.<T>builder()
                .code(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .message("has not authority to operation this service")
                .build();
    }

    public static <T> ApiRes<T> notFound() {
        return notFound("resource is  not found");
    }

    public static <T> ApiRes<T> notFound(String msg) {
        return ApiRes.<T>builder()
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .message(msg)
                .build();
    }

    public static <T> ApiRes failed() {
        return failed("operation is failed");
    }

    public static <T> ApiRes failed(String msg) {
        return ApiRes.<T>builder()
                .code("500")
                .message(msg)
                .build();
    }

    public static <T> ApiRes success() {
        return success("operation is success");
    }

    public static <T> ApiRes success(String msg) {
        return ApiRes.<T>builder()
                .code(ApiRes.CODE_SUCCESS)
                .message(msg)
                .build();
    }
    public static <T> ApiRes successData(T data) {
        return successData("operation is success", data);
    }


    public static <T> ApiRes successData(String msg, T data) {
        return ApiRes.<T>builder()
                .code(ApiRes.CODE_SUCCESS)
                .message(msg)
                .data(data)
                .build();
    }
}


