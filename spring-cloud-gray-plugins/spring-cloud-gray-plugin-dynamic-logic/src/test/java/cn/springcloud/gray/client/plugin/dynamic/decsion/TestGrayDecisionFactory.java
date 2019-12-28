package cn.springcloud.gray.client.plugin.dynamic.decsion;

import cn.springcloud.gray.decision.GrayDecision;
import cn.springcloud.gray.decision.factory.AbstractGrayDecisionFactory;
import cn.springcloud.gray.dynamiclogic.DynamicStringArgs;

/**
 * @author saleson
 * @date 2019-12-28 17:29
 */
public class TestGrayDecisionFactory extends AbstractGrayDecisionFactory<DynamicStringArgs> {

    public TestGrayDecisionFactory() {
        super(DynamicStringArgs.class);
    }

    @Override
    public GrayDecision apply(DynamicStringArgs configBean) {
        return args -> {
            System.out.println(configBean);
            System.out.println(args.getGrayRequest());
            System.out.println(args.getServer());
            return true;
        };
    }
}
