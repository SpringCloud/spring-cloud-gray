package cn.springcloud.gray.concurrent;

import lombok.Data;

@Data
public class GrayRunnableContext extends GrayAsyncContext {

    private Runnable target;

}
