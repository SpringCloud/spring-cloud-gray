package cn.springcloud.gray.mock;

import cn.springcloud.gray.request.GrayRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author saleson
 * @date 2020-05-25 20:37
 */
@Data
@AllArgsConstructor
@Builder
public class GrayReuqestMockInfo implements MockInfo {
    private GrayRequest grayRequest;
}
