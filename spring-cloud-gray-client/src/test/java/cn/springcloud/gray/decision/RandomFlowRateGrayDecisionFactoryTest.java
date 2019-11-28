package cn.springcloud.gray.decision;

import cn.springcloud.gray.decision.factory.FlowRateGrayDecisionFactory;
import cn.springcloud.gray.decision.factory.RandomFlowRateGrayDecisionFactory;
import cn.springcloud.gray.request.GrayHttpRequest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author saleson
 * @date 2019-11-28 15:17
 */
public class RandomFlowRateGrayDecisionFactoryTest {

    @Test
    public void testAllowRandomFlowRateGrayDecision() {
        RandomFlowRateGrayDecisionFactory decisionFactory = new RandomFlowRateGrayDecisionFactory();

        RandomFlowRateGrayDecisionFactory.Config config = new RandomFlowRateGrayDecisionFactory.Config();
        config.setSalt("");
        config.setType(FlowRateGrayDecisionFactory.FIELD_SCOPE_HTTP_HEADER);
        config.setField("mark");
        config.setValue("abc");
        config.setRate(99);

        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.addHeader("mark", "abc");
        GrayDecisionInputArgs args = GrayDecisionInputArgs.builder().grayRequest(grayRequest).build();

        GrayDecision grayDecision = decisionFactory.apply(config);
        boolean val = grayDecision.test(args);
        Assert.assertTrue("异常，预期是能通过", val);

        grayRequest.setHeader("mark", "Abc");
        val = grayDecision.test(args);
        Assert.assertFalse("异常，预期是不能通过", val);
        config.setIgnoreCase(true);
        val = grayDecision.test(args);
        Assert.assertTrue("异常，预期是能通过", val);

    }


    @Test
    public void testRefuseRandomFlowRateGrayDecision() {
        RandomFlowRateGrayDecisionFactory decisionFactory = new RandomFlowRateGrayDecisionFactory();

        RandomFlowRateGrayDecisionFactory.Config config = new RandomFlowRateGrayDecisionFactory.Config();
        config.setSalt("");
        config.setType(FlowRateGrayDecisionFactory.FIELD_SCOPE_HTTP_HEADER);
        config.setField("mark");
        config.setValue("abc");
        config.setRate(1);

        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.addHeader("mark", "abc");
        GrayDecisionInputArgs args = GrayDecisionInputArgs.builder().grayRequest(grayRequest).build();

        GrayDecision grayDecision = decisionFactory.apply(config);


        boolean val = grayDecision.test(args);
        Assert.assertFalse("异常，预期是不能通过", val);

    }


    @Test
    public void testRate() {
        double diff = 3.0;
        RandomFlowRateGrayDecisionFactory decisionFactory = new RandomFlowRateGrayDecisionFactory();

        RandomFlowRateGrayDecisionFactory.Config config = new RandomFlowRateGrayDecisionFactory.Config();
        config.setSalt("");
        config.setType(FlowRateGrayDecisionFactory.FIELD_SCOPE_HTTP_HEADER);
        config.setField("mark");
        config.setValue("abc");
        config.setRate(10);

        GrayHttpRequest grayRequest = new GrayHttpRequest();
        grayRequest.addHeader("mark", "abc");
        GrayDecisionInputArgs args = GrayDecisionInputArgs.builder().grayRequest(grayRequest).build();

        GrayDecision grayDecision = decisionFactory.apply(config);
        int count = 0;
        int allowCount = 0;
        for (int i = 0; i < 10000; i++) {
            count++;
            if (grayDecision.test(args)) {
                allowCount++;
            }
        }
        double rate = allowCount * 1.0 / count;
        System.out.println(String.format("count:%d , allowCount:%d , rate:%s ", count, allowCount, rate));


        double maxRate = (config.getRate() + diff) / 100;
        double minRate = (config.getRate() - diff) / 100;

        boolean assertRate = minRate < rate && rate < maxRate;
        Assert.assertTrue("异常！比例相关过大. maxrate:" + maxRate + ", minRate:" + minRate, assertRate);
    }
}
