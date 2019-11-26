package cn.springcloud.gray.server.resources;

import cn.springcloud.gray.api.ApiRes;
import cn.springcloud.gray.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author saleson
 * @date 2019-11-25 21:49
 */
@ControllerAdvice
@Slf4j
public class ExceptionTranslator {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiRes<Void> processNotFoundException(NotFoundException ex) {
        log.error("{}", ex.getMessage(), ex);
        return ApiRes.<Void>builder()
                .code(ApiRes.CODE_NOT_FOUND)
                .message(StringUtils.defaultIfEmpty(ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase()))
                .build();
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes<Void> processIllegalArgumentException(IllegalArgumentException ex) {
        log.error("{}", ex.getMessage(), ex);
        return ApiRes.<Void>builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(StringUtils.defaultIfEmpty(ex.getMessage(), "instance host or port is empty"))
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes<Void> processException(Exception ex) {
        log.error("{}", ex.getMessage(), ex);
        return ApiRes.<Void>builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(StringUtils.defaultIfEmpty(ex.getMessage(), "error"))
                .build();
    }
}
