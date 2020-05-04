package cn.springcloud.gray.decision;

import cn.springcloud.gray.servernode.ServerSpec;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GrayDecisionInputArgs extends DecisionInputArgs {

    private ServerSpec server;
}
