package cn.springcloud.gray.decision;

import cn.springcloud.gray.node.Server;
import cn.springcloud.gray.request.GrayRequest;
import lombok.Data;

@Data
public class GrayDecisionInputArgs {

    private Server server;
    private GrayRequest grayRequest;
}
