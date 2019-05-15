package cn.springcloud.gray.decision;

import cn.springcloud.gray.servernode.ServerSpec;
import cn.springcloud.gray.request.GrayRequest;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GrayDecisionInputArgs {

    private ServerSpec server;
    private GrayRequest grayRequest;
}
