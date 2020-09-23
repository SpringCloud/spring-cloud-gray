package cn.springcloud.gray.event.longpolling;

import lombok.Data;

/**
 * @author saleson
 * @date 2020-02-03 23:32
 */
@Data
public class ListenResult {

    public static final int RESULT_STATUS_TIMEOUT = 8;
    public static final int RESULT_STATUS_HAS_NEWER = 1;

    private int status;

}
