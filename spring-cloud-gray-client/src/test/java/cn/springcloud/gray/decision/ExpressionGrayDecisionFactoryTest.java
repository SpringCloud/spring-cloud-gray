package cn.springcloud.gray.decision;

import cn.springcloud.gray.decision.compare.CompareMode;
import cn.springcloud.gray.decision.factory.ExpressionGrayDecisionFactory;
import cn.springcloud.gray.request.GrayRequest;
import cn.springcloud.gray.request.GrayTrackInfo;
import org.junit.Test;

/**
 * @author saleson
 * @date 2020-09-15 15:47
 */
public class ExpressionGrayDecisionFactoryTest {

    @Test
    public void test() {
        ExpressionGrayDecisionFactory factory = new ExpressionGrayDecisionFactory();

        GrayRequest grayRequest = new GrayRequest();
        GrayTrackInfo grayTrackInfo = new GrayTrackInfo();
        grayRequest.setGrayTrackInfo(grayTrackInfo);
        grayTrackInfo.setAttribute("t", "eq");

        ExpressionGrayDecisionFactory.Config config = new ExpressionGrayDecisionFactory.Config();
        config.setCompareMode(CompareMode.EQUAL);
        config.setValueType(ValueType.SINGLE);
        config.setValue("eq");
//        config.setValueExpression("#gr.grayTrackInfo.attributes['t']");
        config.setValueExpression("#gr.grayTrackInfo?.getAttribute('t')");

        DecisionInputArgs args = new DecisionInputArgs();
        args.setGrayRequest(grayRequest);
        boolean b = factory.apply(config).test(args);
        System.out.println(b);
    }
}
