package cn.springcloud.gray.concurrent;

import lombok.Data;

import java.util.concurrent.Callable;

@Data
public class GrayCallableContext extends GrayAsyncContext {

    private Callable<?> target;

}
